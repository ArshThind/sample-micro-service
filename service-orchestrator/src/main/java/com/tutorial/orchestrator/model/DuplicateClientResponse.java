package com.tutorial.orchestrator.model;

import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Input stream on the original {@link ClientHttpResponse} received after calling the micro-service
 * was getting closed which was throwing exception as it was being read in the zuul {@link org.springframework.cloud.netflix.zuul.filters.post.SendResponseFilter}
 * This is a convenient workaround which copies the response attributes from the original response
 * and creates a duplicate object with a valid input stream which can be then read safely downstream.
 */
@NoArgsConstructor
public class DuplicateClientResponse implements ClientHttpResponse {

    private ClientHttpResponse httpResponse;

    private InputStream copy;

    private HttpStatus httpStatus;
    private int statusCode;
    private String statusText;
    private HttpHeaders httpHeaders;


    public DuplicateClientResponse(ClientHttpResponse httpResponse) throws IOException {
        this.httpResponse = httpResponse;
        this.statusCode = httpResponse.getRawStatusCode();
        this.statusText = httpResponse.getStatusText();
        this.httpHeaders = httpResponse.getHeaders();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(httpResponse.getBody(), outputStream);
        copy = new ByteArrayInputStream(outputStream.toByteArray());
    }

    public DuplicateClientResponse(CustomResponseWrapper customResponseWrapper) {
        this.httpStatus = HttpStatus.valueOf(customResponseWrapper.getStatus());
        this.statusCode = customResponseWrapper.getStatus();
        this.statusText = customResponseWrapper.getStatusInfo();
        this.httpHeaders = unWrapHeaders(customResponseWrapper.getHeaders());
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return httpStatus;
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return statusCode;
    }

    @Override
    public String getStatusText() throws IOException {
        return statusText;
    }

    @Override
    public void close() {
        if (httpResponse != null) {
            httpResponse.close();
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return copy;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    private HttpHeaders unWrapHeaders(Map<String, Object> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue().toString());
            }
        }
        return httpHeaders;
    }
}
