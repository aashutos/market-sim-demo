package com.ntak.example.market_sim.portfolio.controller;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("portfolio-balance-service")
@RibbonClient("portfolio-balance-service")
public interface IBalanceController {
    @RequestMapping(value = "settle", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> settleFunds();

    @RequestMapping(value = "allocate", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> allocateFunds();
}
