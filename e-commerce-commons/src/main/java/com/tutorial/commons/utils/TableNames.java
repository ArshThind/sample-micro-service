package com.tutorial.commons.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that is used to provide dynamic table names at runtime.
 */
@Configuration
@ConfigurationProperties(prefix = "tables")
@Getter
@Setter
@ToString
public class TableNames {

    //=========================================================================
    // ~ STATIC MEMBERS USED FOR HOLDING CONSTANT VALUES

    private static final String USERS = "[USERS]";
    private static final String PRODUCTS = "[PRODUCTS]";
    private static final String ORDER_STATUS = "[ORDER_STATUS]";
    private static final String DEL_ADDRESS = "[DEL_ADDRESS]";
    private static final String ORDERS = "[ORDERS]";

    private String productsTable;
    private String ordersTable;
    private String orderStatusTable;
    private String deliveryAddressTable;
    private String usersTable;

    private final String[] SEARCH_STRINGS = {USERS, PRODUCTS, ORDER_STATUS, DEL_ADDRESS, ORDERS};
    private final String[] REPLACE_STRINGS = {getUsersTable(), getProductsTable(), getOrderStatusTable(), getDeliveryAddressTable(), getOrdersTable()};


    /**
     * Replaces the placeholders in the supplied query with the configured table names.
     *
     * @param query The query for which the table names are to be replaced
     * @return Updated query having with the table names loaded from the application.yaml
     */
    public String replaceTableNames(String query) {
        return StringUtils.replaceEach(query, SEARCH_STRINGS, REPLACE_STRINGS);
    }
}
