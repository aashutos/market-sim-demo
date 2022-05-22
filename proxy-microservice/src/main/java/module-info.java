module proxy.microservice {
    // Module Dependencies
    requires jmod.base.microservice;
    requires org.slf4j;

    // Module Interfaces
    exports com.ntak.example.market_sim.proxy;
    exports com.ntak.example.market_sim.proxy.controller;
    exports com.ntak.example.market_sim.proxy.configuration;

    opens com.ntak.example.market_sim.proxy;
    opens com.ntak.example.market_sim.proxy.controller;
    opens com.ntak.example.market_sim.proxy.configuration;
}