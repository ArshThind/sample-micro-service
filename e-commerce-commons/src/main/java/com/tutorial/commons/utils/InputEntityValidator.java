package com.tutorial.commons.utils;

import com.tutorial.commons.model.BadInputException;

/**
 * Abstract class to be extended by the entity validators to validate the input received.
 *
 * @param <T> The entity to be validated.
 */
public abstract class InputEntityValidator<T> {

    private String message;

    /**
     * The final method which is to be called by the validator.
     *
     * @param t Entity to be validated.
     * @return true if the validation is successful.
     * @throws BadInputException If the input contains invalid parameters.
     */
    public final boolean validateEntity(T t) throws BadInputException {
        if (validateMandatoryParameters(t) && validateOptionalParameters(t)) {
            return true;
        }
        throw new BadInputException(message);
    }

    /**
     * Abstract method to be implemented by the child classes describing the logic to validate optional parameters.
     *
     * @param t Entity to be validated.
     * @return true/false depending on whether the validation is successful or not.
     */
    protected abstract boolean validateOptionalParameters(T t);

    /**
     * Abstract method to be implemented by the child classes describing the logic to validate the mandatory parameters.
     *
     * @param t Entity to be validated.
     * @return true/false depending on whether the validation is successful or not.
     */
    protected abstract boolean validateMandatoryParameters(T t);

    /**
     * Setter method to set the exception message eventually passed to the end user.
     * Child classes should supply this message.
     *
     * @param message
     */
    protected void setMessage(String message) {
        this.message = message;
    }
}
