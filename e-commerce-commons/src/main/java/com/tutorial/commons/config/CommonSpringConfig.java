package com.tutorial.commons.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Spring configuration class which defines all the beans which are common to all the services.
 */
@Configuration
@ComponentScan("com.tutorial.commons")
@Slf4j
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceHealthIndicatorAutoConfiguration.class,
        TransactionAutoConfiguration.class})
public class CommonSpringConfig {

    private static String JDBC_URL_SYNTAX = "jdbc:mysql://%1$s:%2$s/%3$s";

    /**
     * Database configuration bean.
     * Will be constructed only if the application context doesn't have a datasource bean already.
     *
     * @param config HikariCP configuration for the datasource
     * @return HikariCP {@link DataSource} configured for the application.
     */
    @Bean
    public DataSource dataSource(HikariConfig config) {
        log.warn("Configuring the DataSource");
        HikariDataSource dataSource = new HikariDataSource(config);
        log.warn("DataSource configured successfully!");
        return dataSource;
    }

    /**
     * Spring bean containing the data source properties for the configured datasourec.
     *
     * @param properties @{@link DataSourceProperties} object encapsulating the properties of the datasource.
     * @return HikariConfig object to be supplied to a HikariCP datasource implementation.
     */
    @Bean
    @Primary
    public HikariConfig dataSourceConfig(DataSourceProperties properties) {
        log.warn("Database props: {}", properties);
        log.warn(String.format(JDBC_URL_SYNTAX, properties.getHostName(), properties.getPort(), properties.getDatabase()));
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(properties.getUserName());
        config.setPassword(properties.getPassword());
        config.setMaximumPoolSize(properties.getMax());
        config.setMinimumIdle(properties.getMin());
        config.setJdbcUrl(String.format(JDBC_URL_SYNTAX, properties.getHostName(), properties.getPort(), properties.getDatabase()));
        config.setConnectionTestQuery(properties.TEST_QUERY);
        return config;
    }

    /**
     * A  configured {@link NamedParameterJdbcTemplate} bean used to query the underlying <code>dataSource()</code>
     *
     * @param dataSource
     * @return
     */
    @Bean
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Declares a spring @{@link DataSourceTransactionManager} to be utilized via AOP to manage transactions
     *
     * @param dataSource The underlying datasource
     * @return Spring transaction manager for the underlying datasource
     * @throws SQLException
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }
}
