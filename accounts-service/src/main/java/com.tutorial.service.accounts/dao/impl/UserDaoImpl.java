package com.tutorial.service.accounts.dao.impl;

import com.tutorial.commons.model.User;
import com.tutorial.commons.utils.QueryProvider;
import com.tutorial.service.accounts.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

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
    public List<User> getAllUsers() throws IOException {

        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ALL_USERS);
        log.info(query);
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
     * @throws IOException
     */
    @Override
    public User getUser(String userName) throws IOException {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_USER_BY_USERNAME);
        log.info(query);
        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME, userName);
        List<User> users = namedParameterJdbcTemplate.query(query, params, (rs, rowNum) -> {
            return mapUser(rs);
        });
        return users.isEmpty() ? new User() : users.get(0);
    }

    /**
     * {@inheritDoc}
     *
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public int createNewUser(User user) throws IOException {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_NEW_USER);
        log.info(query);
        Map<String, Object> params = new HashMap<>();
        params.put(ID, user.getId());
        params.put(USER_NAME, user.getName());
        params.put(EMAIL, user.getEmail());
        params.put(PHONE, String.valueOf(user.getPhone()));
        int records = namedParameterJdbcTemplate.update(query, params);
        return records;
    }

    /**
     * {@inheritDoc}
     *
     * @param users
     * @return
     * @throws IOException
     */
    @Override
    public int[] createMultipleUsers(List<User> users) throws IOException {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_NEW_USER);
        log.info(query);
        Map<String, Object>[] params = new Map[users.size()];
        int count = 0;
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put(ID, user.getId());
            map.put(USER_NAME, user.getName());
            map.put(EMAIL, user.getEmail());
            map.put(PHONE, String.valueOf(user.getPhone()));
            params[count] = map;
            count++;
        }
        int[] records = namedParameterJdbcTemplate.batchUpdate(query, params);
        return records;
    }

    /**
     * {@inheritDoc}
     *
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public int removeUser(User user) throws IOException {
        String query = queryProvider.getTemplateQuery(QueryProvider.DISABLE_USER);
        log.info(query);
        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME, user.getName());
        return namedParameterJdbcTemplate.update(query, params);
    }

    /**
     * {@inheritDoc}
     *
     * @param users
     * @return
     * @throws IOException
     */
    @Override
    public int removeMultipleUsers(List<User> users) throws IOException {
        String query = "";
        log.info(query);
        String usernames = StringUtils.join(users.stream().map(user -> user.getName()).collect(Collectors.toList()), "','");
        Map<String, String> params = new HashMap<>();
        params.put(USER_NAME, usernames);
        log.info("Params-> {}", params);
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
