//package com.tutorial.orchestrator.filters;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Service
//@Slf4j
//public class OrdersHealthFilter extends ZuulFilter {
//
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    /**
//     * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
//     * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
//     * We also support a "static" type for static responses see  StaticResponseFilter.
//     * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
//     *
//     * @return A String representing that type
//     */
//    @Override
//    public String filterType() {
//        return "route";
//    }
//
//    /**
//     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
//     * important for a filter. filterOrders do not need to be sequential.
//     *
//     * @return the int order of a filter
//     */
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    /**
//     * a "true" return from this method means that the run() method should be invoked
//     *
//     * @return true if the run() method should be invoked. false will not invoke the run() method
//     */
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    /**
//     * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
//     *
//     * @return Some arbitrary artifact may be returned. Current implementation ignores it.
//     * @throws ZuulException if an error occurs during execution.
//     */
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpServletRequest request = context.getRequest();
//        log.info("Request \"{}\" intercepted!", request);
//        System.out.println(request.getRequestURL());
//        return request.getRequestURL();
//    }
//}