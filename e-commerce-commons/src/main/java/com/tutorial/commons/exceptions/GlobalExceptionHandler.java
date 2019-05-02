package com.tutorial.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Utility controller to intercept all the exceptions thrown by a {@link org.springframework.web.bind.annotation.RestController}
 * and provide a meaningful representation to the end user.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all exceptions of type {@link BadInputException}
     *
     * @param bie exception thrown by the controller.
     * @return an instance of {@link ExceptionResponse} wrapped by a Spring {@link ResponseEntity}
     */
    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidInputExceptions(BadInputException bie) {
        return new ResponseEntity<>(ExceptionResponse.buildBadRequestResponse(bie.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all exceptions of type {@link WebApplicationException}
     *
     * @param ex exception thrown by the controller.
     * @return an instance of {@link ExceptionResponse} wrapped by a Spring {@link ResponseEntity}
     */
    @ExceptionHandler(WebApplicationException.class)
    public ResponseEntity<ExceptionResponse> handleWebException(WebApplicationException ex) {
        int status = ex.getResponse().getStatus();
        switch (status) {
            case 204:
                return new ResponseEntity<>(ExceptionResponse.buildNoContentResponse("NO_CONTENT"), HttpStatus.NO_CONTENT);
            case 500:
                return new ResponseEntity<>(ExceptionResponse.buildInternalErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                Response response = ex.getResponse();
                return new ResponseEntity<>(ExceptionResponse.buildCustomResponse
                        (HttpStatus.valueOf(response.getStatus()), response.getStatus(), ex.getMessage()),
                        HttpStatus.valueOf(response.getStatus()));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUncaughtExceptions() {
        ExceptionResponse response = ExceptionResponse.buildInternalErrorResponse("Error! Service Unavailable.");
        return new ResponseEntity<>(response, response.getStatus());
    }
}