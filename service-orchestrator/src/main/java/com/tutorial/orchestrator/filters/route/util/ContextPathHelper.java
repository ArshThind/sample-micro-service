package com.tutorial.orchestrator.filters.route.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility to load the context-paths of all the micro-services
 * registered with eureka server.
 * <p>
 * Runs every 5 minutes and loads the context paths for newly registered services.
 */
@Component
@EnableScheduling
@Slf4j
public class ContextPathHelper {

    private static Map<String, String> CONTEXT_PATH_MAP = new ConcurrentHashMap<>();
    private static final String CONTEXT_PATH = "context-path";
    private static final int DELAY = 5 * 60 * 1000;

    private final Object LOCK = new Object();

    private DiscoveryClient discoveryClient;

    @Autowired
    public ContextPathHelper(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * Triggers a force refresh of the context-paths in case the context path for a particular
     * service is missing.
     */
    public void triggerContextPathRefresh() {
        updateTask().run();
    }

    /**
     * Scheduled task that runs every 5 minutes and pulls the
     * context-paths for the services registered in the registry.
     */
    @Scheduled(fixedDelay = DELAY)
    private void refreshContextPaths() {
        new Thread(updateTask(), "Refresh-Thread").start();
    }

    private void pollRegistry(Map<String, String> paths) {
        List<String> services = discoveryClient.getServices();
        if (CollectionUtils.isNotEmpty(services)) {
            services.forEach(s -> {
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(s);
                if (CollectionUtils.isNotEmpty(serviceInstances)) {
                    ServiceInstance instance = serviceInstances.iterator().next();
                    paths.put(instance.getServiceId().toLowerCase(), instance.getMetadata().getOrDefault(CONTEXT_PATH, StringUtils.EMPTY));
                }
            });
        }
    }

    private void updateContextPaths(Map<String, String> paths) {
        synchronized (LOCK) {
            CONTEXT_PATH_MAP = paths;
        }
    }

    private Runnable updateTask() {
        return () -> {
            log.warn("Updating the context paths..");
            Map<String, String> paths = new HashMap<>();
            pollRegistry(paths);

            updateContextPaths(paths);
            log.warn("Context paths updated successfully.");
            log.info("Context Paths: {}", CONTEXT_PATH_MAP);
        };
    }

    public Map<String, String> getContextPathMap() {
        synchronized (LOCK) {
            return CONTEXT_PATH_MAP;
        }
    }
}
