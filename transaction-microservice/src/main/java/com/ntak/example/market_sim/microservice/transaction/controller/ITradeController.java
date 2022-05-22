package com.ntak.example.market_sim.microservice.transaction.controller;

import com.ntak.example.market_sim.microservice.transaction.model.TransactionRequest;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient("trade-service")
@RibbonClient("trade-service")
public interface ITradeController {
    @RequestMapping("/")
    String rootCall();

    @PostMapping
    @RequestMapping(value = "/transaction", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> serviceInstancesByApplicationName(@Valid @RequestBody TransactionRequest request);
}
