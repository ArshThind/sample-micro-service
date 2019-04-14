package com.tutorial.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility service which provides template queries to for a DAO interface
 */
@Service
@Slf4j
public class QueryProvider {

    private TableNames tableNames;

    @Autowired
    public QueryProvider(TableNames tableNames) {
        this.tableNames = tableNames;
    }

    //===================================================================================
    // ~ USER QUERIES
    public static final String ADD_NEW_USER = "queries/users/add-new-user.sql";
    public static final String DISABLE_USER = "queries/users/disable-user.sql";
    public static final String GET_ALL_USERS = "queries/users/get-all-users-query.sql";
    public static final String GET_USER_BY_USERNAME = "queries/users/get-user-by-username.sql";
    public static final String GET_USER_BY_TYPE = "queries/users/get-users-by-type.sql";
    public static final String GET_USER_BY_ID = "queries/users/get-user-by-id.sql";

    private static final Map<String, String> queries = new HashMap<>();

    /**
     * Initialization method to called immediately after the bean is constructed.
     * This will populate the <code>queries</code> map with the template queries where the key is the query identifier
     * and value is a template query which can be supplied the required parameters at runtime.
     *
     * @throws IOException If the query doesn't exist in the classpath or if it can't be loaded for some reason.
     */
    @PostConstruct
    private void init() throws IOException {
        log.warn("Loading queries from the classpath...");
        queries.put(ADD_NEW_USER, constructQuery(ADD_NEW_USER));
        queries.put(DISABLE_USER, constructQuery(DISABLE_USER));
        queries.put(GET_ALL_USERS, constructQuery(GET_ALL_USERS));
        queries.put(GET_USER_BY_USERNAME, constructQuery(GET_USER_BY_USERNAME));
        queries.put(GET_USER_BY_TYPE, constructQuery(GET_USER_BY_TYPE));
        queries.put(GET_USER_BY_ID, constructQuery(GET_USER_BY_ID));
        log.warn("Queries loaded successfully.");
    }

    /**
     * Method to retrieved a template query based on the query identifier.
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
     * @param query Query having temporary placeholders as table names.
     * @return Updated query with updated table names read from the application.yaml
     * @throws IOException
     */
    private String constructQuery(String query) throws IOException {
        return tableNames.replaceTableNames(IOUtils.toString(new ClassPathResource(query).getURL()));
    }
}
