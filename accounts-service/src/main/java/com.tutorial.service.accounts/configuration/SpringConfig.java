package com.tutorial.service.accounts.configuration;

import lombok.extern.slf4j.Slf4j;
import org.mariadb.jdbc.MariaDbPoolDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Class defining the spring beans.
 */
@Configuration
@ComponentScan("com.tutorial.service.accounts")
@Profile("dev")
@Slf4j
public class SpringConfig {

    /**
     * Hostname for database server.
     */
    @Value("${db.hostname:null}")
    private String hostname;

    /**
     * Database server port
     */
    @Value("${db.port:8080}")
    private int port;

    /**
     * Database name
     */
    @Value("${db.database:test}")
    private String db;

    /**
     * A valid database userName
     */
    @Value("${db.security.userName:root}")
    private String userName;

    /**
     * Database password
     */
    @Value("${db.security.password:}")
    private String password;

    /**
     * Max size for database connection pool
     */
    @Value("${db.pool.max:10}")
    private int maxPoolSize;

    /**
     * Min size for database connection pool
     */
    @Value("${db.pool.min:1}")
    private int minPoolSize;

    /**
     * Database configuration bean.
     *
     * @return {@link DataSource} configured for the application.
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource(hostname, port, db);
        dataSource.setUser(userName);
        dataSource.setPassword(password);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMinPoolSize(minPoolSize);
        log.info("DB status: {}, {}, {}", hostname, port, db);
        return dataSource;
    }

    /**
     * A  configured {@link NamedParameterJdbcTemplate} bean used to query the underlying <code>dataSource()</code>
     *
     * @param dataSource
     * @return
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return namedParameterJdbcTemplate;
    }

    /**
     * Declares a spring @{@link DataSourceTransactionManager} to be utilized via AOP to manage transactions
     *
     * @return
     * @throws SQLException
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }
}
