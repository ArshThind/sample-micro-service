package com.tutorial.service.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Starting point for spring boot application.
 * This will bootstrap the spring application and <code>{@link EnableEurekaClient}</code>
 * will trigger {@link org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration}
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.tutorial.service.accounts", "com.tutorial.commons"})
public class AccountsMainClass {

    public static void main(String[] args) {
        SpringApplication.run(AccountsMainClass.class);
    }
}

