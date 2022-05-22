package com.ntak.example.market_sim.proxy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.SecureRandom;
import java.util.List;

@RestController
public class ProxyController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Validated
    @RequestMapping(path = "/{service}/{resource}")
    public ResponseEntity<String> request(@PathVariable String service, @PathVariable String resource,
            RequestEntity<String> requestEntity) {
        List<ServiceInstance> services = discoveryClient.getInstances(service);

        if (services.isEmpty()) {
            // Raise Exception
            ResponseEntity<String> responseEntity = new ResponseEntity<>(String.format("Error: Service %s not found.", service),
                                                                         HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        HttpMethod method = requestEntity.getMethod();
        URI url = services.get(0).getUri();
        url = url.resolve(String.format("/%s", resource));
        HttpHeaders headers = requestEntity.getHeaders();
        String body = requestEntity.getBody();
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);

        return responseEntity;
    }
}
