package com.ntak.example.market_sim.proxy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@Configuration
public class GeneralConfig {

    @Bean("plain-rest-template")
    public RestTemplate plainRestTemplate(@Autowired RestTemplateBuilder builder) {
        return builder.build();
    }
}
