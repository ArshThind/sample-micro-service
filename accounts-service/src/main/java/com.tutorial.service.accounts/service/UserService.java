package com.tutorial.service.accounts.service;

import com.tutorial.commons.model.User;
import org.springframework.transaction.annotation.Transactional;

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
     */
    List<User> getAllUsers();

    /**
     * Retrieves the user registered with the service based on the userName
     *
     * @param userName
     * @return
     */
    User getUserByUsername(String userName);

    /**
     * Registers a new user with the service.
     *
     * @param user
     * @return
     */
    int createUser(User user);

    /**
     * Un-registers a user from the service.
     *
     * @param userId Id of the user to be un-registered.
     * @return
     */
    @Transactional
    int unregisterUser(String userId);

    /**
     * Retrieved a user registered with the service based on the uer id
     *
     * @param userId id of the user
     * @return User with the given id, if present else a dummy user.
     */
    User getUserById(String userId);

    /**
     * Retrieves users registered with the service of a given type
     *
     * @param type type of the user
     * @return ArrayList of users matching the given type if present, else empty list.
     */
    List<User> getUserByType(User.UserType type);
}
