package com.ntak.example.market_sim.portfolio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BalanceController implements IBalanceController {
    @Override
    public ResponseEntity<String> settleFunds() {
        // Stub response...
        ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return response;
    }

    @Override
    public ResponseEntity<String> allocateFunds()  {
        // Stub response...
        ResponseEntity<String> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return response;
    }
}
