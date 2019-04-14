package com.tutorial.service.products.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Spring configuration class containing the bean definitions
 * and other related configurations.
 */
@Configuration
@Profile("dev")
@ComponentScan("com.tutorial.commons.config")
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ProductsSpringConfig {

    @Autowired
    DataSource ds4;

}
