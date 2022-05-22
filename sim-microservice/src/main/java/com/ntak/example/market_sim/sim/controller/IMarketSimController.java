package com.ntak.example.market_sim.sim.controller;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("market-sim-service")
@RibbonClient("market-sim-service")
public interface IMarketSimController {
    @RequestMapping(value = "match", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> matchTransaction();
}
