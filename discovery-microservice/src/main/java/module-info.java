module discovery.microservice {
    // Module Dependencies
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.web;
    requires spring.aop;

    requires spring.boot.starter;
    requires spring.boot.actuator;
    requires spring.boot.starter.cache;
    requires spring.boot.starter.web;
    requires spring.boot.starter.tomcat;
    requires spring.boot.starter.json;
    requires spring.boot.starter.logging;

    requires spring.cloud.starter;
    requires spring.cloud.commons;
    requires spring.cloud.context;
    requires spring.cloud.netflix.eureka.server;
    requires spring.cloud.netflix.eureka.client;
    requires spring.cloud.starter.netflix.eureka.server;
    requires spring.cloud.loadbalancer;
    requires spring.cloud.starter.loadbalancer;

    // Module Interfaces
    exports com.ntak.example.market_sim;
    opens com.ntak.example.market_sim;
}