package com.ntak.example.market_sim.microservice.transaction.configuration;

import com.ntak.example.market_sim.microservice.transaction.model.TransactionRequest;
import com.ntak.example.market_sim.util.FlowState;
import com.ntak.example.market_sim.util.MicroServiceCompositeCall;
import com.ntak.example.market_sim.util.MicroServiceCompositionCallBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TradeCompositeCallConfig {

    @Bean("transaction-composite-call")
    public MicroServiceCompositeCall<String> genTransactionCompositeCall(@Autowired @Qualifier("secure-random") SecureRandom secureRandom) {
        return MicroServiceCompositionCallBuilder.newInstance(TransactionRequest.class, secureRandom)
                                                 .withEndpointCall("static-data-service",  "staticInfo", HttpMethod.GET)
                                                     .withInputAdapter((i) ->
                                                     {
                                                        MicroServiceCompositeCall.RequestData request =
                                                                ((FlowState)i).<MicroServiceCompositeCall.RequestData>getRequest(0).get();

                                                         TransactionRequest rootRequest = request.<TransactionRequest>getBody().get();

                                                         Map<String,Object> uriParameters = new HashMap<>();
                                                         uriParameters.put("type", rootRequest.getInstrumentType());
                                                         uriParameters.put("id", rootRequest.getInstrumentId());

                                                        return new MicroServiceCompositeCall.RequestData(new HttpHeaders(), uriParameters);
                                                     })
                                                     .withOutputAdapter((o) -> o)
                                                 .withEndpointCall("static-data-service",  "staticInfo", HttpMethod.GET)
                                                     .withInputAdapter((i) ->
                                                                   {
                                                                       MicroServiceCompositeCall.RequestData request =
                                                                               ((FlowState)i).<MicroServiceCompositeCall.RequestData>getRequest(0).get();

                                                                       TransactionRequest rootRequest = request.<TransactionRequest>getBody().get();

                                                                       Map<String,Object> uriParameters = new HashMap<>();
                                                                       uriParameters.put("type", "counterparty");
                                                                       uriParameters.put("id", rootRequest.getClientCounterparty());

                                                                       return new MicroServiceCompositeCall.RequestData(new HttpHeaders(), uriParameters);
                                                                   })
                                                     .withOutputAdapter((o) -> o)
                                                 .withEndpointCall("portfolio-service", "allocate", HttpMethod.POST)
                                                     .withInputAdapter((i)->{
                                                         MicroServiceCompositeCall.RequestData data =
                                                                 new MicroServiceCompositeCall.RequestData(new HttpHeaders(), Collections.emptyMap(), "{}", Collections.emptyList());
                                                         return data;
                                                     })
                                                     //.withOutputAdapter((o)->o)
                                                 .withEndpointCall("sim-service", "match", HttpMethod.POST)
                                                 .withEndpointCall("portfolio-service", "allocate", HttpMethod.POST)
                                                     .withInputAdapter((i)->{
                                                         MicroServiceCompositeCall.RequestData data =
                                                                 new MicroServiceCompositeCall.RequestData(new HttpHeaders(), Collections.emptyMap(), "{}", Collections.emptyList());
                                                         return data;
                                                     })
                                                     //.withOutputAdapter((o)->o)
                                                 .withEndpointCall("portfolio-service", "settle", HttpMethod.POST)
                                                     .withInputAdapter((i)->{
                                                         MicroServiceCompositeCall.RequestData data =
                                                                 new MicroServiceCompositeCall.RequestData(new HttpHeaders(), Collections.emptyMap(), "{}", Collections.emptyList());
                                                         return data;
                                                     })
                                                     //.withOutputAdapter((o)->o)
                                                 .build();
    }
}
