package com.tutorial.service.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * The starting point for the Orders Micro Service. This will establish the required Spring configuration and
 * will bootstrap the Spring app and will start an embedded tomcat server.
 * <code>@{@link EnableEurekaClient}</code> set up the configuration for a eureka discovery client which can be
 * used to look up services registered with a Eureka Server.
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.tutorial.commons", "com.tutorial.service.order"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
public class OrdersMainClass {

    /**
     * Entry point for the JVM.
     *
     * @param args arguments passed via command line.
     */
    public static void main(String[] args) {
        SpringApplication.run(OrdersMainClass.class);
    }

    /**
     * Embedded data source to be used during development.
     *
     * @return H2 datasource
     */
//    @Bean
//    @Primary
//    public DataSource testDataSource() {
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        return builder.setType(EmbeddedDatabaseType.H2)
//                .build();
//    }
}
