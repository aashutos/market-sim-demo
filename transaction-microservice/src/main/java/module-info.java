module transaction.microservice {
    // Module Dependencies
    requires jmod.base.microservice;

    requires spring.cloud.netflix.ribbon;
    requires spring.cloud.starter.netflix.ribbon;
    requires spring.cloud.openfeign.core;
    requires spring.cloud.starter.openfeign;

    requires org.slf4j;

    // Module Interfaces
    opens com.ntak.example.market_sim.microservice.transaction;
    opens com.ntak.example.market_sim.microservice.transaction.controller;
    opens com.ntak.example.market_sim.microservice.transaction.configuration;
    opens com.ntak.example.market_sim.microservice.transaction.model;

    exports com.ntak.example.market_sim.microservice.transaction.handler;
    exports com.ntak.example.market_sim.microservice.transaction.model;
}