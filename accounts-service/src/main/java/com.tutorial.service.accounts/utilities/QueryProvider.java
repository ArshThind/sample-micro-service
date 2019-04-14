package com.tutorial.service.accounts.utilities;

import com.tutorial.service.accounts.configuration.TableNames;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility service which provides template queries to for a {@link com.tutorial.service.accounts.dao.UserDao}
 */
@Service
public class QueryProvider {

    private TableNames tableNames;

    public QueryProvider(TableNames tableNames) {
        this.tableNames = tableNames;
    }

    public static final String ADD_NEW_USER = "queries/add-new-user.sql";
    public static final String DELETE_MULTIPLE_USER = "queries/delete-multiple-users.sql";
    public static final String DELETE_SINGLE_USER = "queries/delete-user.sql";
    public static final String GET_ALL_USERS = "queries/get-all-users-query.sql";
    public static final String GET_USER_BY_USERNAME = "queries/get-user-by-username.sql";

    private static final Map<String, String> queries = new HashMap<>();

    /**
     * Initialization method to called immediately after the bean is constructed.
     * This will populate the <code>queries</code> map with the template queries where the key is the query identifier
     * and value is a template query which can be supplied the required parameters at runtime.
     *
     * @throws IOException
     */
    @PostConstruct
    private void init() throws IOException {
        queries.put(ADD_NEW_USER, constructQuery(ADD_NEW_USER));
        queries.put(DELETE_MULTIPLE_USER, constructQuery(DELETE_MULTIPLE_USER));
        queries.put(DELETE_SINGLE_USER, constructQuery(DELETE_SINGLE_USER));
        queries.put(GET_ALL_USERS, constructQuery(GET_ALL_USERS));
        queries.put(GET_USER_BY_USERNAME, constructQuery(GET_USER_BY_USERNAME));
    }

    /**
     * Method to retrieved a tenplate query based on the query identifier.
     *
     * @param query The key corresponding to the query which is to be retrieved
     * @return The template query corresponding to the supplied key or throws an {@link IllegalArgumentException}
     * if the key doesn't exist.
     */
    public String getTemplateQuery(String query) {
        if (queries.containsKey(query)) {
            return queries.get(query);
        }
        throw new IllegalArgumentException("No such query exists.");
    }

    /**
     * Utility method to inject table names into a query.
     *
     * @param query
     * @return
     * @throws IOException
     */
    private String constructQuery(String query) throws IOException {
        return tableNames.replaceTableNames(IOUtils.toString(new ClassPathResource(query).getURL()));
    }
}
