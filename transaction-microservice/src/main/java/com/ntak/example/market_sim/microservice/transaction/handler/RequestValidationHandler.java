package com.ntak.example.market_sim.microservice.transaction.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestValidationHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleRequestError(BindException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage;
        if (result.hasErrors()) {
            errorMessage = String.format("Errors detected in request body:\n%s\n",
                                                result.getAllErrors()
                                                      .stream()
                                                      .map(e -> e.getDefaultMessage())
                                                      .collect(Collectors.joining(
                                                              "\n")));
        } else {
            errorMessage = "Unknown issue with request occurred.";
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
