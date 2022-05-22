package com.ntak.example.market_sim.microservice.transaction.controller;

import com.ntak.example.market_sim.microservice.transaction.model.TransactionRequest;
import com.ntak.example.market_sim.util.FlowState;
import com.ntak.example.market_sim.util.MicroServiceCompositeCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TradeController implements ITradeController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("transaction-composite-call")
    private MicroServiceCompositeCall<String> cCallTransaction;

    @Override
    public String rootCall() {
        return "Trade-Controller Microservice";
    }

    @Override
    public ResponseEntity<String> serviceInstancesByApplicationName(@Valid @RequestBody TransactionRequest request) {
        ResponseEntity<String> response = new ResponseEntity<>("Success", HttpStatus.OK);
        Map<String,Object> urlParameters = new HashMap<>();

        MicroServiceCompositeCall.RequestData requestData =
                new MicroServiceCompositeCall.RequestData(HttpHeaders.EMPTY, urlParameters, request, Collections.emptyList());
        FlowState rsp = cCallTransaction.execute(discoveryClient,
                                                 restTemplate,
                                                 requestData
        );

        // Parse response...

        return response;
    }
}
