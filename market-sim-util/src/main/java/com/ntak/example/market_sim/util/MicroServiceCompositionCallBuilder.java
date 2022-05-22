package com.ntak.example.market_sim.util;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.function.Function;

/**
 *   Constructs a composite microservice call integrating interactions with multiple sequential calls to form a final
 *   response state.
 *
 */
public class MicroServiceCompositionCallBuilder<T> {
    private final MicroServiceCompositeCall<T> construct;

    private MicroServiceCompositionCallBuilder(Class<T> requestType) {
        this.construct = new MicroServiceCompositeCall(requestType);
    }

    private MicroServiceCompositionCallBuilder(Class<T> requestType, SecureRandom secureRandom) {
        this.construct = new MicroServiceCompositeCall(requestType, secureRandom);
    }

    public static <T> MicroServiceCompositionCallBuilder<T> newInstance(Class<T> requestType) {
        return new MicroServiceCompositionCallBuilder(requestType);
    }

    public static <T> MicroServiceCompositionCallBuilder<T> newInstance(Class<T> requestType, SecureRandom secureRandom) {
        return new MicroServiceCompositionCallBuilder(requestType, secureRandom);
    }

    /**
     *   Adds an endpoint call step to the request.
     *
     *   @param serviceName Service to lookup via the Eureka Discovery Client.
     *   @param resource The Resource to make request to within the service aforementioned.
     *   @param method The HTTP method for the request call.
     *   @return MicroserviceCompositionCallBuilder (builder step)
     */
    public MicroServiceCompositionCallBuilder withEndpointCall(String serviceName, String resource, HttpMethod method) {
        construct.addSequentialCalls(new MicroServiceCompositeCall.MSCallInfo(String.format("%s-%s:%d", serviceName, resource,
                                                                                            construct.getSequentialCallCount()),
                                                                              serviceName,
                                                                              resource,
                                                                              method));

        return this;
    }

    /**
     *   Specify a method to extract data from FlowState object to form the appropriate request for current endpoint
     *   call construction.
     *
     *   A NOP if {@link withEndpointCall()} was not made beforehand.
     *   @param inputAdapter Extractor function to extract request object from state
     *   @param <R> Request object body POJO type for next call
     *   @return MicroserviceCompositionCallBuilder (builder step)
     */
    public <R> MicroServiceCompositionCallBuilder withInputAdapter(Function<FlowState,R> inputAdapter) {
        Optional<MicroServiceCompositeCall.MSCallInfo> optCall;

        if ((optCall = construct.getCallAtIndex(construct.getSequentialCallCount()-1)).isPresent()) {
            MicroServiceCompositeCall.MSCallInfo call = optCall.get();
            call.setInputAdapter(inputAdapter);
        }

        return this;
    }

    /**
     *   Specify a method to stores the output from Http Response into a custom bean and added on to the FlowState
     *   response chain for current endpoint call construction.
     *
     *   A NOP if {@link withEndpointCall()} was not made beforehand.
     *
     *   @param outputAdapter Ingests HTTP ResponseEntity with generic type, T body.
     *   @return MicroserviceCompositionCallBuilder (builder step)
     */
    public MicroServiceCompositionCallBuilder withOutputAdapter(Function<ResponseEntity<Object>,Object> outputAdapter) {
        Optional<MicroServiceCompositeCall.MSCallInfo> optCall;

        if ((optCall = construct.getCallAtIndex(construct.getSequentialCallCount()-1)).isPresent()) {
            MicroServiceCompositeCall.MSCallInfo call = optCall.get();
            call.setOutputAdapter(outputAdapter);
        }

        return this;
    }

    public MicroServiceCompositeCall build() {
        return construct;
    }
}
