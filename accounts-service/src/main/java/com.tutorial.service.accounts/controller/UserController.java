package com.tutorial.service.accounts.controller;

import com.tutorial.service.accounts.model.User;
import com.tutorial.service.accounts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * A REST based Spring controller that provides api end points to perform operations related to user accounts.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * REST endpoint to query all the users registered with the service
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<User> getAllUsers() throws IOException {
        return userService.getAllUsers();
    }

    /**
     * REST endpoint to query a user based on the username.
     *
     * @param userName
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public User getUser(@PathVariable("username") String userName) throws IOException {
        return userService.getUser(userName);
    }

    /**
     * REST endpoint to register a new user with the service.
     *
     * @param user
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public int createUser(@RequestBody User user) throws IOException {
        return userService.createUser(user);
    }

    /**
     * REST endpoint to register multiple users with the service.
     *
     * @param users
     * @return
     * @throws IOException
     */
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String createMultipleUsers(@RequestBody List<User> users) throws IOException {
        return userService.addMultipleUsers(users);
    }

    /**
     * REST endpoint to delete a user from the underlying store
     * <p>
     * NOTE: This will not actually delete the user from the underlying data store but will
     * actually mark the user account as inactive.
     *
     * @param user
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public int deleteUser(@RequestBody User user) throws IOException {
        return userService.removeUser(user);
    }

    /**
     * REST endpoint to delete multiple users from the underlying datastore.
     * NOTE: This will not actually delete the user from the underlying data store but will
     * actually mark the user accounts as inactive.
     * @param users
     * @return
     * @throws IOException
     */
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public int deleteMultipleUsers(@RequestBody List<User> users) throws IOException {
        return userService.removeMultipleUsers(users);
    }
}
