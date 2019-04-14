package com.tutorial.commons.model;

/**
 * Exception class representing the exception to be thrown when invalid input is received.
 */
public class BadInputException extends Exception {

    /**
     * Parameterized constructor to pass the message to super class.
     *
     * @param message
     */
    public BadInputException(String message) {
        super(message);
    }
}
