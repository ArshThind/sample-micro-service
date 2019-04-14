package com.tutorial.commons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;

/**
 * Spring configuration class that reads database properties from a application.yml or application.properties file.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dataSource.props", ignoreUnknownFields = false)
@ConditionalOnMissingBean(DataSource.class)
@ToString
public class DataSourceProperties {

    public static final String TEST_QUERY = "SELECT 1";
    /**
     * Database server hostname
     */
    private String hostName;

    /**
     * Server port number
     */
    private int port;

    /**
     * Database name
     */
    private String database;

    /**
     * A valid userName
     */
    private String userName;

    /**
     * Password for the database
     */
    private String password;

    /**
     * Minimum pool size
     */
    private int min = 1;

    /**
     * Maximum pool size
     */
    private int max = 10;
}
