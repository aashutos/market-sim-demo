module market.sim.util {
    // Module Dependencies
    requires spring.cloud.netflix.eureka.client;
    requires spring.cloud.starter;
    requires spring.cloud.commons;
    requires spring.cloud.context;
    requires spring.web;
    requires org.slf4j;

    // Module Interfaces
    exports com.ntak.example.market_sim.util;
    exports com.ntak.example.market_sim.util.model;
}