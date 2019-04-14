package com.tutorial.orchestrator.configs;

import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.websocket.ClientEndpointConfig;

@Configuration
//@EnableAutoConfiguration(exclude = GsonAutoConfiguration.class)
public class GateKeeperConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

//    @Bean
//    public RibbonClientConfiguration ribbonClientConfiguration() {
//        RibbonClientConfiguration configuration = new RibbonClientConfiguration();
//        configuration.ribbonServerList()
//    }
}
