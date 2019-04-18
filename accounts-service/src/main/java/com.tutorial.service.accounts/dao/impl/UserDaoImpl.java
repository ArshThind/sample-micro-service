package com.tutorial.service.accounts.dao.impl;

import com.tutorial.commons.annotations.DaoProfiler;
import com.tutorial.commons.model.User;
import com.tutorial.commons.utils.QueryProvider;
import com.tutorial.service.accounts.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tutorial.service.accounts.configuration.Constants.*;

/**
 * Default implementation of the UserDao interface
 */
@Repository
@Slf4j
public class UserDaoImpl implements UserDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private QueryProvider queryProvider;

    @Autowired
    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, QueryProvider queryProvider) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
        this.queryProvider = queryProvider;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws IOException
     */
    @Override
    @DaoProfiler(queryName = "get-all-users")
    public List<User> getAllUsers() {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ALL_USERS);
        List<User> users = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, rs -> {
            users.add(new User(rs));
        });
        return users;
    }

    /**
     * {@inheritDoc}
     *
     * @param userName
     * @return
     */
    @Override
    @DaoProfiler(queryName = "get-user-by-username")
    public User getUserByUsername(String userName) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_USER_BY_USERNAME);
        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME, userName);
        List<User> users = namedParameterJdbcTemplate.query(query, params, (rs, rowNum) -> {
            return mapUser(rs);
        });
        return users.isEmpty() ? User.createDummyUser() : users.get(0);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Id of the user
     * @return
     */
    @DaoProfiler(queryName = "get-user-by-userId")
    @Override
    public User getUserById(String userId) {
        String templateQuery = queryProvider.getTemplateQuery(QueryProvider.GET_USER_BY_ID);
        Map<String, String> params = new HashMap<>(1);
        params.put(USER_ID, userId);
        User user = namedParameterJdbcTemplate.query(templateQuery, params, rs -> {
            if (rs.next()) {
                return mapUser(rs);
            }
            return User.createDummyUser();
        });
        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @param userType
     * @return
     */
    @Override
    @DaoProfiler(queryName = "get-users-by-type")
    public List<User> getUsersByType(User.UserType userType) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_USER_BY_TYPE);
        Map<String, String> params = new HashMap<>(1);
        params.put(USER_TYPE, userType.getValue());
        return namedParameterJdbcTemplate.query(query, params, (rs, rowNum) -> {
            return mapUser(rs);
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param user
     * @return
     */
    @DaoProfiler(queryName = "add-new-user")
    @Override
    public int createNewUser(User user) {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_NEW_USER);
        Map<String, Object> params = new HashMap<>();
        params.put(USER_NAME, user.getName());
        params.put(EMAIL, user.getEmail());
        params.put(PHONE, String.valueOf(user.getPhone()));
        params.put(USER_TYPE, user.getUserType());
        int records = namedParameterJdbcTemplate.update(query, params);
        return records;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId
     * @return
     */
    @DaoProfiler(queryName = "disable-user")
    @Override
    public int disableUserAccount(String userId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.DISABLE_USER);
        Map<String, String> params = new HashMap<>();
        params.put(USER_ID, userId);
        return namedParameterJdbcTemplate.update(query, params);
    }

    /**
     * Helper method to create a new user from a @{@link ResultSet}
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private User mapUser(ResultSet rs) throws SQLException {
        return new User(rs);
    }
}
