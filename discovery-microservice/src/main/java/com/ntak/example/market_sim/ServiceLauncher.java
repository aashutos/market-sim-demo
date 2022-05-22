package com.ntak.example.market_sim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceLauncher {
    public static void main(String[] args) {
        SpringApplication.run(ServiceLauncher.class, args);
    }
}
