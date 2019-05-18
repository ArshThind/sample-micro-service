package com.tutorial.orchestrator.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.client.ClientHttpResponse;

import java.util.List;
import java.util.Map;

/**
 * Simple POJO class to deserialize response for PUT, POST and DELETE calls.
 * Was having trouble to map the response for calls made for PUT,POST and DELETE methods, so had to put
 * in a hack to deserialize the response.
 */
@Getter
@Setter
public class CustomResponseWrapper {

    private int status;
    private int length;
    private String language;
    private String location;
    private String date;
    private String lastModified;
    private String statusInfo;
    private String mediaType;
    private Map<String, Object> metadata;
    private List<String> allowedMethods;
    private Map<String, String> cookies;
    private List<String> links;
    private Map<String, Object> headers;

    public ClientHttpResponse getResponse() {
        return new DuplicateClientResponse(this);
    }
}