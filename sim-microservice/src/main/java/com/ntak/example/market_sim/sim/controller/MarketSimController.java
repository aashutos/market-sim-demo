package com.ntak.example.market_sim.sim.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MarketSimController implements IMarketSimController {
    @Override
    public ResponseEntity<String> matchTransaction() {
        // Stub response...
        ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return response;
    }
}
