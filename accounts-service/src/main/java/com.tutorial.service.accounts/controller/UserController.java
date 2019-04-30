package com.tutorial.service.accounts.controller;

import com.tutorial.commons.exceptions.BadInputException;
import com.tutorial.commons.model.User;
import com.tutorial.service.accounts.service.UserService;
import com.tutorial.service.accounts.utils.UserInputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * A REST based Spring controller that provides api end points to perform operations related to user accounts.
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private UserService userService;

    private UserInputValidator validator;

    private static String CUSTOMER = "C";

    @Autowired
    public UserController(UserService userService, UserInputValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    /**
     * REST endpoint to query all the users registered with the service OR to query users based on userType OR multiple userIds
     *
     * @return ArrayList of users if users exist, else HTTP Status 204
     */
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<User> getUsers(@QueryParam("userType") String userType, @QueryParam("userIds") String userIds) {
        try {
            int requestType = validator.validateGetUserRequest(userType, userIds);
            List<User> users;
            switch (requestType) {
                case 1:
                    users = userService.getAllUsers();
                    break;
                case 2:
                    users = userService.getUserByType(determineUserType(userType));
                    break;
                case 3:
                    users = userService.getUsersByIds(userIds);
                    break;
                default:
                    throw new WebApplicationException(Response.Status.NO_CONTENT);

            }
            if (users.size() > 0) {
                return users;
            }
        } catch (BadInputException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
        throw new WebApplicationException(Response.Status.NO_CONTENT);
    }

    /**
     * REST endpoint to query a user based on the username.
     *
     * @param userName username of the user
     * @return User with the given username, if exists, else returns HTTP Status 204
     */
    @RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public User getUsersByUsername(@QueryParam("username") String userName) {
        try {
            validator.validateUsernameInput(userName);
            User user = userService.getUserByUsername(userName);
            if (user != null) {
                return user;
            }
        } catch (BadInputException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
        throw new WebApplicationException(Response.Status.NO_CONTENT);
    }

    /**
     * REST endpoint to register a new user with the service.
     *
     * @param user user to be created
     * @return {@link Response} encapsulating the http status
     */
    @RequestMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response createUser(@RequestBody User user) {
        try {
            validator.validateInputUser(user);
            if (userService.createUser(user) > 0) {
                return Response.status(Response.Status.CREATED).build();
            }
        } catch (BadInputException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }

    /**
     * REST endpoint to remove a user from the service
     * <p>
     * NOTE: This will not actually delete the user from the underlying data store but will
     * mark the user account as inactive.
     *
     * @param userId Id of the user to be removed from the service
     * @return {@link Response} encapsulating the http status
     */
    @RequestMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public Response removeUser(@PathVariable("userId") String userId) {
        try {
            validator.validateUserId(userId);
            if (userService.unregisterUser(userId) > 1) {
                return Response.status(Response.Status.OK).build();
            }
        } catch (BadInputException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }

    /**
     * REST endpoint to retrieve a user based on the userId
     *
     * @param userId Id of the user
     * @return User corresponding to the id if found, else HTTP Status 204
     */
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("userId") String userId) {
        try {
            validator.validateUserId(userId);
            User user = userService.getUserById(userId);
            if (user != null) {
                return user;
            }
        } catch (BadInputException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
        throw new WebApplicationException(Response.Status.NO_CONTENT);
    }

    /**
     * Utility method to determine {@link com.tutorial.commons.model.User.UserType} for an incoming request.
     *
     * @param userType userType passed as input.
     * @return UserType corresponding to the passed input.
     */
    private User.UserType determineUserType(String userType) {
        return userType.equals(CUSTOMER) ? User.UserType.CUSTOMER : User.UserType.VENDOR;
    }
}
