package com.ntak.example.market_sim.sim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.security.SecureRandom;
import java.util.Properties;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SimServiceLauncher {
    private static SecureRandom RNG = new SecureRandom();

    private static final Logger LOGGER = LoggerFactory.getLogger(SimServiceLauncher.class);

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        try (InputStream is = SimServiceLauncher.class.getResourceAsStream("/sim-service.properties")) {
            props.load(is);
        }
        int minPort = Integer.parseInt(props.getProperty("server.port.range.min"));
        int maxPort = Integer.parseInt(props.getProperty("server.port.range.max"));
        int retry = Integer.parseInt(props.getProperty("server.port.scan.retry"));
        int port = minPort + RNG.nextInt(maxPort-minPort);

        do {
            try(ServerSocket ss = new ServerSocket(port)) {
                break;
            } catch (IOException e) {
            }

            LOGGER.debug(String.format("Retrying launch of Market Simulation MicroService (Port Number: %s). Attempt: %d",
                                       System.getProperty("server.port"),
                                       retry));
        } while (retry > 0);

        SpringApplication app = new SpringApplication(SimServiceLauncher.class);
        if (props.getProperty("server.port") == null) {
            props.put("server.port", String.valueOf(port));
        }
        app.setDefaultProperties(props);
        app.run();
    }
}
