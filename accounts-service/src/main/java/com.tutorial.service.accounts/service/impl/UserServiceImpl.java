package com.tutorial.service.accounts.service.impl;

import com.tutorial.service.accounts.dao.UserDao;
import com.tutorial.service.accounts.model.User;
import com.tutorial.service.accounts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Base implementation of the @{@link UserService}
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param userDao
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
     * @throws IOException
     */
    @Override
    public List<User> getAllUsers() throws IOException {
        return userDao.getAllUsers();
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param userName
     * @return
     * @throws IOException
     */
    @Override
    public User getUser(String userName) throws IOException {
        return userDao.getUser(userName);
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public int createUser(User user) throws IOException {
        return userDao.createNewUser(user);
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param users
     * @return
     * @throws IOException
     */
    @Override
    public String addMultipleUsers(List<User> users) throws IOException {
        return Arrays.toString(userDao.createMultipleUsers(users));
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public int removeUser(User user) throws IOException {
        return userDao.removeUser(user);
    }

    /**
     * {@inheritDoc}
     * Delegates to {@link UserDao}
     *
     * @param users
     * @return
     * @throws IOException
     */
    @Override
    public int removeMultipleUsers(List<User> users) throws IOException {
        return userDao.removeMultipleUsers(users);
    }
}
