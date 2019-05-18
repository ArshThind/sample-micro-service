package com.tutorial.orchestrator.filters.route;

import com.netflix.client.ClientException;
import com.netflix.zuul.context.RequestContext;
import com.tutorial.orchestrator.filters.route.util.ContextPathHelper;
import com.tutorial.orchestrator.model.CustomResponseWrapper;
import com.tutorial.orchestrator.model.DuplicateClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.HttpMethod.*;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Encapsulates the logic for routing.
 * <p>
 * By default, zuul registers a route filter - {@link RibbonRoutingFilter}, which performs the routing
 * based on the serviceIds registered in the eureka server and also performs the load-balancing using a
 * ribbon load balancer.
 * <p>
 * Since zuul doesn't return the context-path in the base uri while polling the service registry by default,
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
        String payload = setRequestPayload(request);
        try {
            response = invokeMicroService(requestUri, method, payload);
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

    private ClientHttpResponse invokeMicroService(String requestUri, String method, String payload) throws IOException, URISyntaxException {
        HttpHeaders headers = getHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        switch (method.toUpperCase()) {
            case GET:
                return restTemplate.execute(requestUri, HttpMethod.valueOf(method), null, this::createDuplicateResponse, new Object[0]);
            case POST:
            case PUT:
            case DELETE:
                ResponseEntity<CustomResponseWrapper> response = restTemplate.exchange(requestUri, HttpMethod.valueOf(method), httpEntity, CustomResponseWrapper.class);
                return response.getBody().getResponse();
            default:
                throw new WebApplicationException(Response.Status.METHOD_NOT_ALLOWED);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(ACCEPT, APPLICATION_JSON_VALUE);
        httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

    private String setRequestPayload(HttpServletRequest request) throws IOException {
        String payload = request.getReader() == null ? null : IOUtils.toString(request.getReader());
        return payload;
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
        return new DuplicateClientResponse(httpResponse);
    }
}
