package com.ntak.example.market_sim.microservice.static_data.controller;

import com.ntak.example.market_sim.util.model.StaticInfoResponse;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("static-data-service")
@RibbonClient("static-data-service")
public interface IStaticDataController {
    @RequestMapping("/")
    String rootCall();

    @RequestMapping(value = "/staticInfo", method = RequestMethod.GET)
    ResponseEntity<StaticInfoResponse> getStaticInfo(@RequestParam String type, @RequestParam String id);
}

