module jmod.base.microservice {
    // Module Dependencies
    requires transitive org.hibernate.validator;
    requires transitive java.validation;

    requires transitive spring.core;
    requires transitive spring.web;
    requires transitive spring.beans;
    requires transitive spring.context;

    requires transitive spring.cloud.commons;

    requires transitive spring.boot;
    requires transitive spring.boot.autoconfigure;
    requires transitive spring.boot.actuator;
    requires transitive spring.boot.actuator.autoconfigure;
    requires transitive spring.boot.starter.actuator;

    requires transitive market.sim.util;
}