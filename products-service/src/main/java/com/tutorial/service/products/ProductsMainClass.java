package com.tutorial.service.products;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * The starting point for the Products Micro Service. This will establish the required Spring configuration and
 * will bootstrap the Spring app and will start an embedded tomcat server.
 * <code>@{@link EnableEurekaClient}</code> set up the configuration for a eureka discovery client which can be
 * used to look up services registered with a Eureka Server.
 */

@SpringBootApplication
@EnableEurekaClient
@Slf4j
@ComponentScan(basePackages = {"com.tutorial.commons","com.tutorial.service.products"})
public class ProductsMainClass {

    public static void main(String[] args) {
        SpringApplication.run(ProductsMainClass.class);
        log.info("Products Service started successfully!");
    }
}
