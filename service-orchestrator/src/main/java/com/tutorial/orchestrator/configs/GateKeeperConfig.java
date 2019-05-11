package com.tutorial.orchestrator.configs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Spring class containing the core spring configuration.
 */
@Configuration
@Slf4j
public class GateKeeperConfig {

    /**
     * {@link ZuulProperties} default bean created by spring boot auto configuration.
     */
    @Autowired
    private ZuulProperties zuulProperties;

    /**
     * {@link DiscoveryClient} default bean created by spring boot to discover micro services registered
     * in the service registry.
     */
    @Autowired
    private DiscoveryClient discoveryClient;

    private static final String CONTEXT_PATH = "context-path";

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    /**
     * Currently discovery client doesn't returns the context-path of the services registered with the
     * eureka server. This is convenient workaround to attach the service context-path to the URIs of
     * services registered with the eureka.
     */
    @PostConstruct
    private void initializeZuulRoutes() {
        log.warn("Configuring Zuul Routes");
        zuulProperties.getRoutes().values().forEach(s -> discoveryClient.getInstances(s.getServiceId()).stream()
                .forEach(
                        instance -> {
                            String basePath = instance.getUri().toString();
                            String serviceURL = basePath.concat(instance.getMetadata().getOrDefault(CONTEXT_PATH, StringUtils.EMPTY));
                            s.setUrl(serviceURL);
                        }));
        log.warn("Zuul Routes configured successfully: {}",zuulProperties);
    }
}
