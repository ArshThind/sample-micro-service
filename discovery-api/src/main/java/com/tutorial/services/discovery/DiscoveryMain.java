package com.tutorial.services.discovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Starting point for the Spring boot application.
 * <code>@EnableEurekaServer</code> will trigger the {@link org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration}
 * and will set up a Eureka Registry Server.
 */
@SpringBootApplication
@Slf4j
@EnableEurekaServer
public class DiscoveryMain {

    public static void main(String[] args) {
        log.info("Starting up the discovery server...");
        SpringApplication.run(DiscoveryMain.class);
        log.info("Discovery server started successfully!!");
    }
}
