package com.tutorial.service.order;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@RestController
public class MainClass {

    public static void main(String[] args) {
        BeanFactory factory = SpringApplication.run(MainClass.class).getBeanFactory();
        DiscoveryClient discoveryClient = factory.getBean(DiscoveryClient.class);
        System.out.println(discoveryClient.getInstances("accounts"));
    }

    @RequestMapping("/status")
    public String getStatus() {
        return "1";
    }
}
