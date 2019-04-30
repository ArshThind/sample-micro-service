package com.tutorial.service.accounts.utils;

import com.tutorial.commons.exceptions.BadInputException;
import com.tutorial.commons.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Utility service which provides validation services to validate input passed to a
 * {@link com.tutorial.service.accounts.controller.UserController}
 */
@Service
@Slf4j
public class UserInputValidator {

    //Acceptable inputs for user type
    private static final Set<String> VALID_USER_TYPE = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("C", "V")));

    //Regex for valid e-mail
    private static final String E_MAIL_REGEX = "[A-Za-z0-9_]{4,30}[@][a-z]+(.com)";

    /**
     * Validates the username passed as input
     *
     * @param username value passed to the controller
     * @return true if username is valid, else throws {@link BadInputException}
     */
    public boolean validateUsernameInput(String username) {
        log.info("Validating username for input: {}", username);
        if (!StringUtils.isBlank(username)) {
            return true;
        }
        throw new BadInputException("username cannot be empty.", Response.Status.BAD_REQUEST);
    }

    /**
     * Validates the inputs passed to {@link com.tutorial.service.accounts.controller.UserController}#getUsers
     *
     * @param userType A valid userType
     * @param userIds  comma separated list of user ids
     * @return
     */
    public int validateGetUserRequest(String userType, String userIds) {
        if (checkIfAllUserRequest(userType, userIds)) {
            return 1;
        }
        if (!StringUtils.isAnyBlank(userIds, userType)) {
            throw new BadInputException("Input should contain either 'userIds' OR 'userType'", Response.Status.BAD_REQUEST);
        }
        if (!StringUtils.isBlank(userType)) {
            if (!VALID_USER_TYPE.contains(userType))
                throw new BadInputException("userType should be in " + VALID_USER_TYPE, Response.Status.BAD_REQUEST);
            return 2;
        }
        if (validateUserIds.test(userIds)) {
            return 3;
        }
        return -1;
    }

    /**
     * Validates the input passed to create User method
     *
     * @param user An instance of {@link User}
     * @return true if input is valid, else throws a {@link BadInputException}
     */
    public boolean validateInputUser(User user) {
        if (StringUtils.isAnyEmpty(user.getName(), user.getEmail(), String.valueOf(user.getPhone()))
                || user.getUserType() == null) {
            throw new BadInputException("No field should be empty.", Response.Status.BAD_REQUEST);
        }
        if (!validatePhone.test(user.getPhone())) {
            throw new BadInputException("Please pass a valid phone number", Response.Status.BAD_REQUEST);
        }
        if (!validateEmail.test(user.getEmail())) {
            throw new BadInputException("Please pass a valid e-mail", Response.Status.BAD_REQUEST);
        }
        return true;
    }

    /**
     * Validates the userId passed to a {@link com.tutorial.service.accounts.controller.UserController} methods
     *
     * @param userId numerical id of the user
     * @return true if input is valid, else throws a {@link BadInputException}
     */
    public boolean validateUserId(String userId) {
        try {
            Integer.parseInt(userId);
        } catch (NumberFormatException nfe) {
            throw new BadInputException("userId should be a number.", Response.Status.BAD_REQUEST);
        }
        return true;
    }

    /**
     * Utility method to check if the input request is for retrieving all users
     *
     * @param userType
     * @param userIds
     * @return
     */
    private boolean checkIfAllUserRequest(String userType, String userIds) {
        return StringUtils.isAllBlank(userIds, userType);
    }

    /**
     * Validates the phone number passed as input
     */
    private Predicate<Long> validatePhone = s -> String.valueOf(s).length() == 10;

    /**
     * Validates the e-mail passed as input
     */
    private Predicate<String> validateEmail = s -> s.matches(E_MAIL_REGEX);

    /**
     * Validates the comma separated userIds one by one
     */
    private Predicate<String> validateUserIds = s -> {
        String[] ids = StringUtils.split(s, ",");
        for (String id : ids) {
            validateUserId(id);
        }
        return true;
    };
}
