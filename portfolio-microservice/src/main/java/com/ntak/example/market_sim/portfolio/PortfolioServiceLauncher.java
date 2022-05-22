package com.ntak.example.market_sim.portfolio;

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
public class PortfolioServiceLauncher {
    private static SecureRandom RNG = new SecureRandom();

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioServiceLauncher.class);

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        try (InputStream is = PortfolioServiceLauncher.class.getResourceAsStream("/portfolio-service.properties")) {
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

            LOGGER.debug(String.format("Retrying launch of Portfolio MicroService (Port Number: %s). Attempt: %d",
                                       System.getProperty("server.port"),
                                       retry));
        } while (retry > 0);

        SpringApplication app = new SpringApplication(PortfolioServiceLauncher.class);
        if (props.getProperty("server.port") == null) {
            props.put("server.port", String.valueOf(port));
        }
        app.setDefaultProperties(props);
        app.run();
    }
}
