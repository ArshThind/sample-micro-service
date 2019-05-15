package com.tutorial.orchestrator.filters.route;

import com.netflix.client.ClientException;
import com.netflix.zuul.context.RequestContext;
import com.tutorial.orchestrator.filters.route.util.ContextPathHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

/**
 * Encapsulates the logic for routing.
 * <p>
 * By default, zuul registers a route filter - {@link RibbonRoutingFilter}, which performs the routing
 * based on the serviceIds registered in the eureka server and also performs the load-balancing using a
 * ribbon load balancer.
 * <p>
 * Since zuul doesn't returns the context-path in the base uri while polling the service registry by default,
 * this class offers a convenient workaround.
 * <p>
 * Working:
 * 1. Since we are extending the {@link RibbonRoutingFilter}, we only override the methods that we need, thereby leaving
 * other methods untouched.
 * 2. Determine the serviceId for the incoming request.
 * 3. Forward the request to the origin.
 * 4. Add additional zuul headers and update the {@link RequestContext} with necessary values so that it doesn't break
 * the existing zuul flow.
 */
@Component
@Slf4j
public class OriginRouter extends RibbonRoutingFilter {

    private static final String PREFIX = "rest/api";
    private static final String EQUALS = "=";
    private static final String AMPERSAND = "&";
    private static final String QUERY_START = "?";

    private final RestTemplate restTemplate;
    private ContextPathHelper contextPathHelper;

    @Autowired
    public OriginRouter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory, List<RibbonRequestCustomizer> requestCustomizers,
                        RestTemplate template, ContextPathHelper contextPathHelper) {
        super(helper, ribbonCommandFactory, requestCustomizers);
        this.restTemplate = template;
        this.contextPathHelper = contextPathHelper;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        try {
            this.helper.addIgnoredHeaders();
            ClientHttpResponse response = forward(context);
            setResponse(response);
            return response;
        } catch (WebApplicationException e) {
            throw e;
        } catch (ClientException e) {
            log.error("Got client exception while invoking {}, Error: {}", context.get(SERVICE_ID_KEY).toString().toLowerCase(),
                    e);
            throw new WebApplicationException(e);
        } catch (IOException e) {
            log.error("Got I/O exception while invoking {}, Error: {}", context.get(SERVICE_ID_KEY).toString().toLowerCase(),
                    e);
            throw new WebApplicationException(e);
        }
    }

    private ClientHttpResponse forward(RequestContext context) throws IOException {
        String serviceId = context.get(SERVICE_ID_KEY).toString().toLowerCase();
        String contextPath = getContextPath(serviceId);
        String method = context.getRequest().getMethod();
        HttpServletRequest request = context.getRequest();
        MultiValueMap<String, String> params = this.helper
                .buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        if (request.getContentLength() < 0 && !verb.equalsIgnoreCase("GET")) {
            context.setChunkedRequestBody();
        }

        String uri = this.helper.buildZuulRequestURI(request);
        uri = StringUtils.substringAfter(uri, PREFIX);

        String requestUri = getRequestUri(serviceId, contextPath, uri, params);
        ClientHttpResponse response;
        try {
            response = restTemplate.execute(requestUri, HttpMethod.valueOf(method), null, this::createDuplicateResponse
                    , new Object[0]);
        } catch (WebApplicationException e) {
            log.error("Received exception from micro-service: {}, Error: {}, status: {}", serviceId, e, e.getResponse().getStatus());
            throw e;
        } catch (HttpClientErrorException e) {
            log.error("Error while invoking {}, Error: {}, status: {}", serviceId, e, e.getRawStatusCode());
            throw new WebApplicationException(e.getRawStatusCode());
        } catch (Exception e) {
            log.error("Unknown Exception occurred while invoking {}, Error: {}, status: {}", serviceId, e, 500);
            throw new WebApplicationException(e.getCause(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private String getContextPath(String serviceId) {
        Map<String, String> contextPathMap = this.contextPathHelper.getContextPathMap();
        if (!contextPathMap.containsKey(serviceId)) {
            this.contextPathHelper.triggerContextPathRefresh();
        }
        String contextPath = contextPathMap.get(serviceId);
        if (contextPath == null) {
            log.error("No Servers available for service: {}", serviceId);
            throw new WebApplicationException("No Servers available for " + serviceId,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return contextPath;
    }

    private String getRequestUri(String serviceId, String contextPath, String uri, MultiValueMap<String, String> params) {
        StringBuilder uriBuilder = new StringBuilder("http://");
        uriBuilder.append(serviceId).append(contextPath).append(uri).append(QUERY_START);

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                uriBuilder.append(entry.getKey()).append(EQUALS).append(StringUtils.join(entry.getValue(), ","));
                uriBuilder.append(AMPERSAND);
            }
        }
        return uriBuilder.toString().substring(0, uriBuilder.length() - 1);
    }

    private ClientHttpResponse createDuplicateResponse(ClientHttpResponse httpResponse) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(httpResponse.getBody(), outputStream);
        InputStream copy = new ByteArrayInputStream(outputStream.toByteArray());
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return httpResponse.getStatusCode();
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return httpResponse.getRawStatusCode();
            }

            @Override
            public String getStatusText() throws IOException {
                return httpResponse.getStatusText();
            }

            @Override
            public void close() {
                httpResponse.close();
            }

            @Override
            public InputStream getBody() throws IOException {
                return copy;
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpResponse.getHeaders();
            }
        };
    }
}
