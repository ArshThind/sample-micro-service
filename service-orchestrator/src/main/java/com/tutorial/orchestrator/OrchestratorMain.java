package com.tutorial.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * The core class for the reverse proxy. This will setup a reverse zuul proxy and
 * will route the requests to micro-services registered with eureka server.
 */
@EnableEurekaClient
@SpringBootApplication
@EnableZuulProxy
@Slf4j
public class OrchestratorMain {

    public static void main(String[] args) {
        log.info("Starting up Orchestrator...");
        SpringApplication.run(OrchestratorMain.class);
        log.info("Orchestrator started successfully!!");
    }
}
