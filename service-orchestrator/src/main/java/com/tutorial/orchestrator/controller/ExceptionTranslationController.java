package com.tutorial.orchestrator.controller;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

/**
 * Errors thrown by Zuul are not intercepted by the {@link org.springframework.web.bind.annotation.ControllerAdvice}
 * annotated handlers present within the Spring context by default. Instead zuul itself propagates the errors to the
 * end user. This sort of defeats the purpose of having a global {@link org.springframework.web.bind.annotation.ControllerAdvice}
 * in a micro-service application.
 * <p>
 * Workaround:
 * 1. Errors in zuul are handled by {@link org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter}, which
 * forwards the request with embedded exception information to '/error' path.
 * 2. Register a Spring {@link org.springframework.boot.web.servlet.error.ErrorController} which handles the requests
 * to '/error' path within the spring context.
 * 3. All the requests with errors will be forwarded to the error controller, which can then be used to translate and
 * re-throw the exceptions so that they can be handled by the Global {@link org.springframework.web.bind.annotation.ControllerAdvice}
 * registered within the application.
 */
@RestController
public class ExceptionTranslationController extends AbstractErrorController {

    private static final String ERROR_PATH = "/error";

    public ExceptionTranslationController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * Intercepts the requests containing the relevant exception information and extracts the underlying exception
     * wrapped by the {@link com.netflix.zuul.exception.ZuulException} which is then re-thrown.
     *
     * @param request the request for which the exception has occurred.
     */
    @RequestMapping(value = ERROR_PATH)
    public void error(HttpServletRequest request) throws Exception {
        Throwable t = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (t instanceof WebApplicationException) {
            WebApplicationException we = (WebApplicationException) t.getCause();
            throw we;
        }
        Exception exception = (WebApplicationException) t.getCause();
        throw exception;
    }
}
