package com.tutorial.service.accounts.service.impl;

import com.tutorial.commons.model.User;
import com.tutorial.service.accounts.dao.UserDao;
import com.tutorial.service.accounts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Base implementation of the @{@link UserService}
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    /**
     * Parameterized constructor for wiring in a @{@link UserDao}
     *
     * @param userDao The dao implementation to be wired in.
     */
    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @return
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param userName
     * @return
     */
    @Override
    public User getUserByUsername(String userName) {
        return userDao.getUserByUsername(userName);
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param user
     * @return
     */
    @Override
    public int createUser(User user) {
        return userDao.createNewUser(user);
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param userId
     * @return
     */
    @Override
    public int unregisterUser(String userId) {
        return userDao.disableUserAccount(userId);
    }

    /**
     * {@inheritDoc}
     * Delegates to a @{@link UserDao}
     *
     * @param userId id of the user
     * @return
     */
    @Override
    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    /**
     * {@inheritDoc}
     * Delegates to a @{@link UserDao}
     *
     * @param type id of the user
     * @return
     */
    @Override
    public List<User> getUserByType(User.UserType type) {
        return userDao.getUsersByType(type);
    }

}
