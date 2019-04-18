package com.tutorial.service.accounts.controller;

import com.tutorial.commons.model.User;
import com.tutorial.commons.utils.InputEntityValidator;
import com.tutorial.service.accounts.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * A REST based Spring controller that provides api end points to perform operations related to user accounts.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    private InputEntityValidator validator;

    private static String CUSTOMER = "customer";

    private static String VENDOR = "vendor";

    public UserController(UserService userService, InputEntityValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    /**
     * REST endpoint to query all the users registered with the service
     *
     * @return
     */
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<User> getAllUsers(@QueryParam("userType") String userType) {
        return StringUtils.isEmpty(userType) ? userService.getAllUsers() :
                userService.getUserByType(CUSTOMER.equals(userType) ? User.UserType.CUSTOMER : User.UserType.VENDOR);
    }

    /**
     * REST endpoint to query a user based on the username.
     *
     * @param userName
     * @return
     */
    @RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public User getUser(@QueryParam("username") String userName) {
        return userService.getUserByUsername(userName);
    }

    /**
     * REST endpoint to register a new user with the service.
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public int createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * REST endpoint to remove a user from the service
     * <p>
     * NOTE: This will not actually delete the user from the underlying data store but will
     * mark the user account as inactive.
     *
     * @param userId Id of the user to be removed from the service
     * @return
     */
    @RequestMapping(value = "users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public int removeUser(@PathVariable("userId") String userId) {
        return userService.unregisterUser(userId);
    }

    /**
     * REST endpoint to retrieve a user based on the userId
     *
     * @param userId Id of the user
     * @return User corresponding to the id if found, else HTTP Status 204
     */
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId);
    }
}
