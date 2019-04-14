package com.tutorial.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@Slf4j
public class OrchestratorMain {

    public static void main(String[] args) {
        log.info("Starting up Orchestrator...");
        SpringApplication.run(OrchestratorMain.class);
        log.info("Orchestrator started successfully!!");
    }
}
