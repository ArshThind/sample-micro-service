package com.tutorial.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Exception class representing the exception to be thrown when invalid input is received.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadInputException extends WebApplicationException {

    public BadInputException(String message, Response.Status status) {
        super(message, status);
    }

    public BadInputException(String message) {
        this(message, Response.Status.BAD_REQUEST);
    }
}
