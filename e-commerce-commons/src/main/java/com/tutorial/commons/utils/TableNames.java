package com.tutorial.commons.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that is used to provide dynamic table names at runtime.
 */
@Configuration
@ConfigurationProperties(prefix = "tables")
public class TableNames {

    /**
     * Replaces the placeholders in the supplied query with the configured table names.
     *
     * @param query
     * @return
     */
    public String replaceTableNames(String query) {
        return null;
    }
}
