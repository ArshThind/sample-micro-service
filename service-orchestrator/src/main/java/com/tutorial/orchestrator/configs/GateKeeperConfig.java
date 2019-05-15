package com.tutorial.orchestrator.configs;

import com.tutorial.commons.exceptions.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * Spring class containing the core spring configuration.
 */
@Configuration
@Slf4j
@ComponentScan(basePackageClasses = GlobalExceptionHandler.class)
public class GateKeeperConfig {

    /**
     * Spring rest template to be used for load balancing while calling the micro-services.
     *
     * @return A load balancer aware {@link RestTemplate}
     */
    @Bean
    @LoadBalanced
    @Primary
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
