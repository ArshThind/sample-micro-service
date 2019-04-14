package com.tutorial.service.accounts.service;

import com.tutorial.service.accounts.dao.UserDao;
import com.tutorial.service.accounts.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Defines the contract for the to be implemented by the implementations
 * which provide services relating to @{@link User}
 */
public interface UserService {

    /**
     * Retrieves all the users registered with the service.
     *
     * @return
     * @throws IOException
     */
    List<User> getAllUsers() throws IOException;

    /**
     * Retrieves the user registered with the service based on the userName
     *
     * @param userName
     * @return
     * @throws IOException
     */
    User getUser(String userName) throws IOException;

    /**
     * Registers a new user with the service.
     *
     * @param user
     * @return
     * @throws IOException
     */
    int createUser(User user) throws IOException;

    /**
     * Registers multiple users with the service.
     *
     * @param users
     * @return
     * @throws IOException
     */
    @Transactional
    String addMultipleUsers(List<User> users) throws IOException;

    /**
     * Un-registers a user from the service.
     *
     * @param user
     * @return
     * @throws IOException
     */
    @Transactional
    int removeUser(User user) throws IOException;

    /**
     * Un-registers multiple users from the service.
     *
     * @param users
     * @return
     * @throws IOException
     */
    @Transactional
    int removeMultipleUsers(List<User> users) throws IOException;
}
