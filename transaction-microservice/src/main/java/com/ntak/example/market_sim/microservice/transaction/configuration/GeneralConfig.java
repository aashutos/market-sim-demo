package com.ntak.example.market_sim.microservice.transaction.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@Configuration
public class GeneralConfig {
    @Bean
    @Scope("singleton")
    public RestTemplateBuilder getRestTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean("plain-rest-template")
    public RestTemplate plainRestTemplate(@Autowired RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("secure-random")
    @Scope("singleton")
    public SecureRandom genSecureRandom(@Value("${secure.random.algorithm:NativePRNGNonBlocking}") String algorithm) {
        try {
            return SecureRandom.getInstance(algorithm);
        } catch(Exception e) {
            return new SecureRandom();
        }
    }
}
