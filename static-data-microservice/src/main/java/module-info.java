module static_data.microservice{
        // Module Dependencies
        requires jmod.base.microservice;

        requires spring.cloud.netflix.ribbon;
        requires spring.cloud.starter.netflix.ribbon;
        requires spring.cloud.openfeign.core;
        requires spring.cloud.starter.openfeign;
    requires org.slf4j;

    // Module Interfaces
        opens com.ntak.example.market_sim.microservice.static_data;
        opens com.ntak.example.market_sim.microservice.static_data.controller;
        //opens com.ntak.example.market_sim.microservice.static_data.configuration;
}
