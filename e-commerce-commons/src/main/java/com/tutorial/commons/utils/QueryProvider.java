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
    public static final String GET_USER_LIST_BY_USER_IDS = "queries/users/get-users-by-id-set.sql";

    //===================================================================================
    // ~ ORDER QUERIES
    public static final String GET_ALL_ORDERS = "queries/orders/get-all-orders.sql";
    public static final String GET_ORDERS_BY_USER = "queries/orders/get-orders-by-user.sql";
    public static final String GET_ORDERS_BY_PRODUCT = "queries/orders/get-orders-by-product.sql";
    public static final String GET_ORDER_BY_ID = "queries/orders/get-orders-by-id.sql";
    public static final String ADD_NEW_ORDER = "queries/orders/add-new-order.sql";
    public static final String ADD_PRODUCT_TO_ORDER = "queries/orders/add-product-to-order.sql";
    public static final String GET_ORDER_COUNT = "queries/orders/check-order-count.sql";
    public static final String CANCEL_ORDER = "queries/orders/cancel-order.sql";
    public static final String ADD_STATUS_ROW = "queries/orders/add-order-status.sql";
    public static final String ADD_ADDRESS_ROW = "queries/orders/add-order-address.sql";

    //=====================================================================================
    // ~ PRODUCT QUERIES
    public static final String GET_ALL_PRODUCTS = "queries/products/get-all-products.sql";
    public static final String GET_PRODUCT_BY_ID = "queries/products/get-product-by-id.sql";
    public static final String GET_PRODUCTS_BY_CATEGORY = "queries/products/get-products-by-category.sql";
    public static final String ADD_NEW_PRODUCT = "queries/products/add-new-product.sql";
    public static final String REMOVE_PRODUCT = "queries/products/remove-product.sql";
    public static final String GET_PRODUCTS_BY_ID_SET = "queries/products/get-products-by-id-set.sql";

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
        //=================================================================================
        //~ LOAD USER QUERIES
        queries.put(ADD_NEW_USER, constructQuery(ADD_NEW_USER));
        queries.put(DISABLE_USER, constructQuery(DISABLE_USER));
        queries.put(GET_ALL_USERS, constructQuery(GET_ALL_USERS));
        queries.put(GET_USER_BY_USERNAME, constructQuery(GET_USER_BY_USERNAME));
        queries.put(GET_USER_BY_TYPE, constructQuery(GET_USER_BY_TYPE));
        queries.put(GET_USER_BY_ID, constructQuery(GET_USER_BY_ID));
        queries.put(GET_USER_LIST_BY_USER_IDS, constructQuery(GET_USER_LIST_BY_USER_IDS));

        //=================================================================================
        //~ LOAD ORDER QUERIES
        queries.put(GET_ALL_ORDERS, constructQuery(GET_ALL_ORDERS));
        queries.put(GET_ORDERS_BY_USER, constructQuery(GET_ORDERS_BY_USER));
        queries.put(GET_ORDERS_BY_PRODUCT, constructQuery(GET_ORDERS_BY_PRODUCT));
        queries.put(GET_ORDER_BY_ID, constructQuery(GET_ORDER_BY_ID));
        queries.put(ADD_NEW_ORDER, constructQuery(ADD_NEW_ORDER));
        queries.put(ADD_PRODUCT_TO_ORDER, constructQuery(ADD_PRODUCT_TO_ORDER));
        queries.put(GET_ORDER_COUNT, constructQuery(GET_ORDER_COUNT));
        queries.put(CANCEL_ORDER, constructQuery(CANCEL_ORDER));
        queries.put(ADD_STATUS_ROW, constructQuery(ADD_STATUS_ROW));
        queries.put(ADD_ADDRESS_ROW, constructQuery(ADD_ADDRESS_ROW));

        //=================================================================================
        //~ LOAD PRODUCT QUERIES
        queries.put(GET_ALL_PRODUCTS, constructQuery(GET_ALL_PRODUCTS));
        queries.put(GET_PRODUCT_BY_ID, constructQuery(GET_PRODUCT_BY_ID));
        queries.put(GET_PRODUCTS_BY_CATEGORY, constructQuery(GET_PRODUCTS_BY_CATEGORY));
        queries.put(ADD_NEW_PRODUCT, constructQuery(ADD_NEW_PRODUCT));
        queries.put(REMOVE_PRODUCT, constructQuery(REMOVE_PRODUCT));
        queries.put(GET_PRODUCTS_BY_ID_SET, constructQuery(GET_PRODUCTS_BY_ID_SET));

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
        log.error("Error while loading the query: {}", query);
        throw new IllegalArgumentException("No such query exists.");
    }

    /**
     * Utility method to inject table names into a query.
     *
     * @param query Query having temporary placeholders as table names.
     * @return Updated query with updated table names read from the application.yaml
     * @throws IOException if an I/O error occurs while loading the query
     */
    private String constructQuery(String query) throws IOException {
        return tableNames.replaceTableNames(IOUtils.toString(new ClassPathResource(query).getURL()));
    }
}
