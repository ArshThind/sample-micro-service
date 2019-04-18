package com.tutorial.service.accounts.dao;

import com.tutorial.commons.model.User;

import java.io.IOException;
import java.util.List;

/**
 * Base Interface to implement user operations on the underlying data source.
 */
public interface UserDao {

    /**
     * Method to retrieve all users from the underlying database.
     *
     * @return ArrayList of User
     */
    List<User> getAllUsers();

    /**
     * Method to retrieve a user from the database based on the username.
     *
     * @param userName
     * @return
     */
    User getUserByUsername(String userName);

    /**
     * Adds a new user record to the database.
     *
     * @param user
     * @return
     */
    int createNewUser(User user);

    /**
     * Marks the corresponding user record in the database as inactive.
     *
     * @param userId Id of the user
     * @return
     * @throws IOException
     */
    int disableUserAccount(String userId);

    /**
     * Retrieves the user records from the database based on the userType
     *
     * @param userType
     * @return
     */
    List<User> getUsersByType(User.UserType userType);

    /**
     * Retrieves the user from the database based on userId
     *
     * @param userId Id of the user
     * @return User matching the userId, if present else returns a dummy user.
     */
    User getUserById(String userId);
}
