package com.tutorial.orchestrator.tasks;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dynamically updates the service-registry with new service instances as they are registered with eureka server.
 * Not a very good implementation though, as it may cause race conditions. This whole exercise is necessary to
 * register the service context-path with the discovery client. Need to find a better alternative.
 * <p>
 * Working:
 * 1. Poll the registry to retrieve the list of all registered services.
 * 2. Prepare a new zuul route map with the service-id as key.
 * 3. Get the context-path and zuul path for the service from the metadata.
 * 4. Update the zuul routes in the zuul properties.
 * <p>
 * Currently, it is scheduled to run at every 5 minutes.
 */

//TODO: Find a better way

@Component
@Slf4j
@EnableScheduling
public class ServiceRegistryUpdater {

    private static final String CONTEXT_PATH = "context-path";

    private static final String PATH = "service-path";

    private static final String ORCHESTRATOR = "ORCHESTRATOR";

    private static final int DELAY = 1 * 60 * 1000;

    private static Map<String, ZuulProperties.ZuulRoute> zuulRouteMap = new HashMap<>();

    private ZuulProperties zuulProperties;

    private DiscoveryClient discoveryClient;

    @Autowired
    public ServiceRegistryUpdater(ZuulProperties zuulProperties, DiscoveryClient discoveryClient) {
        this.zuulProperties = zuulProperties;
        this.discoveryClient = discoveryClient;
    }

    @Scheduled(fixedDelay = DELAY)
    public void refreshRegistry() {
        Thread refreshThread = new Thread(() -> {
            log.warn("Initiating registry refresh");

            pollRegistry();
            updateRoutes();
            updateRegistry();

            log.warn("Service registry updated successfully.");
            log.info("Updated Paths: {}", zuulRouteMap);
        }, "Registry-Refresher");
        refreshThread.start();
    }

    private void pollRegistry() {
        List<String> services = discoveryClient.getServices();
        services.forEach(s -> {
            log.info("Discovered service: {}", s);
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(s);
            if (!s.toUpperCase().equals(ORCHESTRATOR) && CollectionUtils.isNotEmpty(serviceInstances)) {
                ServiceInstance instance = serviceInstances.iterator().next();
                ZuulProperties.ZuulRoute route = createZuulRoute(s, instance);
                zuulRouteMap.put(route.getId(), route);
            }
        });
    }

    private ZuulProperties.ZuulRoute createZuulRoute(String serviceId, ServiceInstance instance) {
        String path = String.join("/"
                , instance.getMetadata().getOrDefault(PATH, StringUtils.EMPTY)
                , "**");
        String id = instance.getMetadata().getOrDefault(PATH, StringUtils.EMPTY);
        id = StringUtils.remove(id, "/");
        ZuulProperties.ZuulRoute route = new ZuulProperties.ZuulRoute(id, path, serviceId,
                null, true, true, null);
        return route;
    }

    private void updateRoutes() {
        zuulRouteMap.values().forEach(s -> discoveryClient.getInstances(s.getServiceId())
                .forEach(
                        instance -> {
                            String basePath = instance.getUri().toString();
                            String serviceURL = basePath.concat(instance.getMetadata()
                                    .getOrDefault(CONTEXT_PATH, StringUtils.EMPTY));
                            s.setUrl(serviceURL);
                        }));
    }

    private void updateRegistry() {
        this.zuulProperties.setRoutes(zuulRouteMap);
    }
}

