module portfolio.microservice {
    // Module Dependencies
    requires jmod.base.microservice;

    requires spring.cloud.netflix.ribbon;
    requires spring.cloud.starter.netflix.ribbon;
    requires spring.cloud.openfeign.core;
    requires spring.cloud.starter.openfeign;

    requires org.slf4j;

    // Module Interfaces
    opens com.ntak.example.market_sim.portfolio;
    opens com.ntak.example.market_sim.portfolio.controller;
}