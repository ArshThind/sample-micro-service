package com.tutorial.service.accounts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Starting point for spring boot application.
 * This will bootstrap the spring application and <code>{@link EnableEurekaClient}</code>
 * will trigger {@link org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration}
 */
@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class AccountsMainClass {

    public static void main(String[] args) {
        SpringApplication.run(AccountsMainClass.class);
    }
}

