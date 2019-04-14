package com.tutorial.services.discovery;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
@EnableEurekaServer
public class DiscoveryMain {

    public static void main(String[] args) {
        log.info("Starting up the discovery server...");
        SpringApplication.run(DiscoveryMain.class);
        log.info("Discovery server started successfully!!");
    }

//    @Bean
//    public ApplicationInfoManager applicationInfoManager(EurekaInstanceConfig instanceConfig, InstanceInfo instanceInfo) {
//        ApplicationInfoManager infoManager = new ApplicationInfoManager(instanceConfig,instanceConfig);
//        return infoManager;
//    }
//
//    @Bean
//    public InstanceInfo instanceInfo(){
//        InstanceInfo instanceInfo=new InstanceInfo()
//    }
}
