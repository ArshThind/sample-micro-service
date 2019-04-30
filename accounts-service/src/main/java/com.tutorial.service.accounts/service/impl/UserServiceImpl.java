package com.tutorial.service.accounts.service.impl;

import com.tutorial.commons.model.User;
import com.tutorial.service.accounts.dao.UserDao;
import com.tutorial.service.accounts.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserByUsername(String userName) {
        return userDao.getUserByUsername(userName);
    }

    @Override
    public int createUser(User user) {
        return userDao.createNewUser(user);
    }

    @Override
    public int unregisterUser(String userId) {
        return userDao.disableUserAccount(userId);
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public List<User> getUserByType(User.UserType type) {
        return userDao.getUsersByType(type);
    }

    @Override
    public List<User> getUsersByIds(String userIds) {
        Set<String> userIdSet = new HashSet<>();
        CollectionUtils.addAll(userIdSet, userIds.split(","));
        return userDao.getUserById(userIdSet);
    }

}
