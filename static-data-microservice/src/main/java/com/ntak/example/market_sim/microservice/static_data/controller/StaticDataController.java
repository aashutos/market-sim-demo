package com.ntak.example.market_sim.microservice.static_data.controller;

import com.ntak.example.market_sim.util.model.StaticInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaticDataController implements IStaticDataController {

    @Override
    public String rootCall() {
        return "Static-Data-Controller Microservice";
    }

    @Override
    public ResponseEntity<StaticInfoResponse> getStaticInfo(@RequestParam String type, @RequestParam String id) {
        switch (type) {
            case "equity":
                StaticInfoResponse resBody = getEquity(id);
                ResponseEntity<StaticInfoResponse> response;

                if (resBody != null) {
                    response = new ResponseEntity<>(resBody, HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
                    resBody.setErrorMessage("ERR02", "Equity does not exist.");
                }
                return response;
            case "counterparty":
                resBody = getCounterparty(id);

                if (resBody != null) {
                    response = new ResponseEntity<>(resBody, HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
                    resBody.setErrorMessage("ERR02", "Counterparty does not exist.");
                }

                return response;
            default:
                resBody = new StaticInfoResponse();
                resBody.setErrorMessage("ERR01", "Invalid static information type.");
                response = new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
                return response;
        }
    }

    private StaticInfoResponse getEquity(String id) {
        StaticInfoResponse response = new StaticInfoResponse();
        response.setIdentifier(id);

        if (id.equals("EQUCMP")) {
            response.addParameter("Name", "The Equity Company, Ordinary Share");
            response.addParameter("IssuerIdentifier", "EQUCMP");
            response.addParameter("ISIN", String.format("UKEQ%s12345", id));
            response.addParameter("Currency", "GBP");
            response.addParameter("TrancheSize", "10000");
            response.addParameter("DividendMonth", "5");
            response.addParameter("DividendPercentRevenue", "0.05");

            return response;
        } else {
            return null;
        }
    }

    private StaticInfoResponse getCounterparty(String id) {
        StaticInfoResponse response = new StaticInfoResponse();
        response.setIdentifier(id);

        if (!id.equals("EQUCMP")) {
            response.addParameter("Identifier", id);
            response.addParameter("Name", String.format("Counterparty %s", id));
            response.addParameter("TradingCurrency", "GBP");
            response.addParameter("EquityIssues", "1");
            response.addParameter("CountryOfIncorporation", "UK");
        } else {
            response.addParameter("Identifier", id);
            response.addParameter("Name", "The Equity Company");
            response.addParameter("TradingCurrency", "GBP");
            response.addParameter("EquityIssues", "1");
            response.addParameter("CountryOfIncorporation", "UK");
        }
        return response;
    }
}
