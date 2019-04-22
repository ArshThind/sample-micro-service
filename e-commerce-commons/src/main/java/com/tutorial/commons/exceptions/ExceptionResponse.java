package com.tutorial.commons.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Simple POJO representing the response model when an exception is thrown.
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

    /**
     * Convenient method to build response for a bad request.
     *
     * @param message Message to be displayed to the user
     * @return an instance of {@link ExceptionResponse}
     */
    public static final ExceptionResponse buildBadRequestResponse(String message) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * Builds response for a server error.
     *
     * @param message Message to be displayed to the user
     * @return an instance of {@link ExceptionResponse}
     */
    public static final ExceptionResponse buildInternalErrorResponse(String message) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    /**
     * Builds response for Http status 204.
     *
     * @param message Message to be displayed to the user
     * @return an instance of {@link ExceptionResponse}
     */
    public static final ExceptionResponse buildNoContentResponse(String message) {
        return new ExceptionResponse(HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), message);
    }

    /**
     * Builds response for the remaining http status codes.
     *
     * @param status     {@link HttpStatus} instance.
     * @param statusCode integer value of the status.
     * @param message    Message to be displayed to the user.
     * @return an instance of {@link ExceptionResponse}
     */
    public static final ExceptionResponse buildCustomResponse(HttpStatus status, int statusCode, String message) {
        return new ExceptionResponse(status, statusCode, message);
    }

    /**
     * {@link HttpStatus} for the response
     */
    private HttpStatus status;

    /**
     * integer value for the returned status
     */
    private int statusCode;

    /**
     * Optional message to be displayed to the end user
     */
    private String message;
}
