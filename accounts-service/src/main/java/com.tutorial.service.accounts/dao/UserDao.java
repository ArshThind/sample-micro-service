package com.tutorial.service.accounts.dao;

import com.tutorial.commons.model.User;

import java.util.List;
import java.util.Set;

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
     * @param userName username to be searched
     * @return user matching the given username if found else null
     */
    User getUserByUsername(String userName);

    /**
     * Adds a new user record to the database.
     *
     * @param user User instance to be created
     * @return 1 if user creation was successful
     */
    int createNewUser(User user);

    /**
     * Marks the corresponding user record in the database as inactive.
     *
     * @param userId Id of the user
     * @return 1 if the user account was successfully disabled, else 0
     */
    int disableUserAccount(String userId);

    /**
     * Retrieves the user records from the database based on the userType
     *
     * @param userType A valid {@link com.tutorial.commons.model.User.UserType}
     * @return ArrayList of Users
     */
    List<User> getUsersByType(User.UserType userType);

    /**
     * Retrieves the user from the database based on userId
     *
     * @param userId Id of the user
     * @return User matching the userId, if present else returns a dummy user.
     */
    User getUserById(String userId);

    /**
     * Retrieves a list of users from the database based on the set of input userIds.
     *
     * @param userIds Set of userIds
     * @return ArrayList of users
     */
    List<User> getUserById(Set<String> userIds);
}
