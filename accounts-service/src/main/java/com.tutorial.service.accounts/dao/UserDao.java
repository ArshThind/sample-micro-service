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
     * @return
     * @throws IOException
     */
    List<User> getAllUsers() throws IOException;

    /**
     * Method to retrieve a user from the database based on the username.
     *
     * @param userName
     * @return
     * @throws IOException
     */
    User getUser(String userName) throws IOException;

    /**
     * Adds a new user record to the database.
     *
     * @param user
     * @return
     * @throws IOException
     */
    int createNewUser(User user) throws IOException;

    /**
     * Adds multiple users to the database.
     *
     * @param users
     * @return
     * @throws IOException
     */
    int[] createMultipleUsers(List<User> users) throws IOException;

    /**
     * Marks the corresponding user record in the database as inactive.
     *
     * @param user
     * @return
     * @throws IOException
     */
    int removeUser(User user) throws IOException;

    /**
     * Marks the records which correspond to the supplied users as inactive.
     *
     * @param users
     * @return
     * @throws IOException
     */
    int removeMultipleUsers(List<User> users) throws IOException;
}
