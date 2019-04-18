package com.tutorial.service.accounts.utils;

import com.tutorial.commons.model.User;
import com.tutorial.commons.utils.InputEntityValidator;
import org.springframework.stereotype.Service;

/**
 * Utility service which provides validation services to validate input passed to a
 * {@link com.tutorial.service.accounts.controller.UserController}
 */
@Service
public class UserInputValidator extends InputEntityValidator<User> {

    /**
     * {@inheritDoc}
     *
     * @param user
     * @return
     */
    @Override
    protected boolean validateOptionalParameters(User user) {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param user
     * @return
     */
    @Override
    protected boolean validateMandatoryParameters(User user) {
        return true;
    }
}
