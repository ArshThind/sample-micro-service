package com.tutorial.commons.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Spring configuration class which defines all the beans which are common to all the services.
 */
@Configuration
@Profile("dev")
@ComponentScan("com.tutorial.commons")
@Slf4j
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class CommonSpringConfig {

    private static String JDBC_URL_SYNTAX = "jdbc:mysql://$1s:$2s/$3s";

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource(DataSourceProperties properties, HikariConfig config) {
        log.warn("Configuring the DataSource");
        HikariDataSource dataSource = new HikariDataSource(config);
        log.warn("DataSource configured successfully!");
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public HikariConfig dataSourceConfig(DataSourceProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setUsername(properties.getUserName());
        config.setPassword(properties.getPassword());
        config.setMaximumPoolSize(properties.getMax());
        config.setMinimumIdle(properties.getMin());
        config.setJdbcUrl(String.format(JDBC_URL_SYNTAX, properties.getHostName(), properties.getPort(), properties.getDatabase()));
        config.setConnectionTestQuery(properties.TEST_QUERY);
        return config;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
