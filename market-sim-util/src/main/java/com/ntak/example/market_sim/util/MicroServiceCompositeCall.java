package com.ntak.example.market_sim.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

/**
 *  Execution mechanism for a composite microservice call.
 */
public class MicroServiceCompositeCall<T> {
    private final SecureRandom RNG;

    private static final Logger LOGGER = LoggerFactory.getLogger(MicroServiceCompositeCall.class);

    private final List<MSCallInfo> sequentialCalls;

    private int i = 0;

    public MicroServiceCompositeCall(Class<T> requestType) {
        this.sequentialCalls = new LinkedList<>();
        this.RNG = new SecureRandom();
        RNG.reseed();
    }

    public MicroServiceCompositeCall(Class<T> requestType, SecureRandom secureRandom) {
        this.sequentialCalls = new LinkedList<>();
        this.RNG = secureRandom;
    }

    public FlowState execute(DiscoveryClient discoveryClient, RestTemplate restTemplate,
            RequestData requestData) {
        FlowState state = new FlowState();
        state.addRequest("init", requestData);

        while (i < sequentialCalls.size()) {
            MSCallInfo call = sequentialCalls.get(i);
            Function<FlowState, RequestData> inputTransform = call.getInputAdapter();
            Function<ResponseEntity<Object>,Object> outputAdapter = call.getOutputAdapter();
            LOGGER.debug(String.format("Executing call (Step %d) on service: %s/%s", i, call.service, call.resource));

            // NOTE: We can use Feign and Ribbon instead of all this...
            // Service discovery and allocation for call...
            List<ServiceInstance> services = discoveryClient.getInstances(call.service);
            int serviceIndex = RNG.nextInt(Math.max(services.size(),0));
            URI serviceURI = discoveryClient.getInstances(call.service).get(serviceIndex).getUri();
            serviceURI = serviceURI.resolve(String.format("/%s", call.resource));

            // Setup input request body (if required) and execute call (Synchronous wait for response)...
            try {
                ResponseEntity<Object> response;
                HttpEntity<Object> httpEntity;

                //  Calls utilise state object to build next request...
                if (inputTransform != null) {
                    requestData = inputTransform.apply(state);
                } else {
                    requestData = new RequestData(new HttpHeaders(), Collections.emptyMap());
                }

                // Add to state object...
                state.addRequest(call.identifier, requestData);

                // Setting HTTP request parameters...
                // HTTP Headers
                HttpHeaders headers;
                if ((headers = requestData.getHeaders()) == null) {
                    headers = new HttpHeaders();
                }

                // URI Parameters
                Map<String,Object> uriParameters;
                if ((uriParameters = requestData.getUriParameters()) == null) {
                    uriParameters = new HashMap<>();
                }

                // Request Body
                Object requestBody = null;
                if (requestData.getBody().isPresent()) {
                    requestBody = requestData.getBody().get();
                }

                if (requestBody == null) {
                    httpEntity = new HttpEntity<>(headers);
                } else {
                    httpEntity = new HttpEntity<>(requestBody, headers);
                }

                // Build URI
                UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(serviceURI.toString());
                uriParameters.forEach((k,v) -> urlBuilder.queryParam(k,v));
                // Path variables
                requestData.getPathVariables().ifPresent(p -> urlBuilder.pathSegment(p.toArray(new String[p.size()])));
                String serviceURIString = urlBuilder.encode()
                                                    .toUriString();

                // Make call to service...
                response = restTemplate.exchange(serviceURIString,
                                                 call.getMethod(),
                                                 httpEntity,
                                                 Object.class,
                                                 uriParameters);

                // Process response as necessary and ingest into response store...
                if (outputAdapter != null) {
                    Object filteredResponse = outputAdapter.apply(response);
                    state.addResponse(call.identifier, filteredResponse);
                } else {
                    state.addResponse(call.identifier, response);
                }

                LOGGER.debug(String.format("Response for service call %s. Code: %d; Message: %s",
                                           call.identifier,
                                           response.getStatusCode()
                                                   .value(),
                                           response.getBody())
                );
            } catch (RestClientException e) {
                LOGGER.debug(String.format("Exception thrown on interaction with endpoint: %s/%s; Exception thrown: %s",
                                           sequentialCalls.get(i).service,
                                           sequentialCalls.get(i).resource,
                                           e.getMessage()));
                throw e;
            }
            i++;
        }

        return state;
    }

    public void addSequentialCalls(MSCallInfo callInfo) {
        sequentialCalls.add(callInfo);
    }

    public Optional<MSCallInfo> getCallAtIndex(int i) {
        if (i < sequentialCalls.size()) {
            return Optional.of(sequentialCalls.get(i));
        }

        return Optional.empty();
    }

    public int getSequentialCallCount() {
        return sequentialCalls.size();
    }

    /**
     *  POJO bean object, which represents a microservice call step as part of composition.
     */
    public static class MSCallInfo<T> {
        private final String identifier;
        private final String service;
        private final String resource;
        private final HttpMethod method;
        private Function<FlowState, RequestData> inputAdapter;
        private Function<ResponseEntity<Object>,Object> outputAdapter;

        public MSCallInfo(String identifier, String service, String resource, HttpMethod method) {
            this(identifier, service, resource, method, null, null);
        }

        public MSCallInfo(String identifier, String service, String resource, HttpMethod method,
                Function<FlowState,RequestData> inputAdapter,
                Function<ResponseEntity<Object>,Object> responseConsumer) {
            this.identifier = identifier;
            this.service = service;
            this.resource = resource;
            this.method = method;
            this.inputAdapter = inputAdapter;
            this.outputAdapter = responseConsumer;
        }

        public void setInputAdapter(Function<FlowState,RequestData> inputAdapter) {
            this.inputAdapter = inputAdapter;
        }

        public void setOutputAdapter(Function<ResponseEntity<Object>,Object> outputAdapter) {
            this.outputAdapter = outputAdapter;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public Function<FlowState,RequestData> getInputAdapter() {
            return inputAdapter;
        }

        public Function<ResponseEntity<Object>,Object> getOutputAdapter() {
            return outputAdapter;
        }
    }

    /**
     *  Bean object containing parameters to be set as part of a forthcoming request.
     */
    public static class RequestData {

        private final HttpHeaders headers;
        private final Map<String,Object> uriParameters;
        private final Object body;
        private final List<String> pathVariables;

        public RequestData(HttpHeaders headers, Map<String,Object> uriParameters) {
            this.headers = headers;
            this.uriParameters = uriParameters;
            this.pathVariables = null;
            this.body = null;
        }

        public RequestData(HttpHeaders headers, Map<String,Object> uriParameters, Object body,
                List<String> pathVariables) {
            this.headers = headers;
            this.uriParameters = uriParameters;
            this.body = body;
            this.pathVariables = pathVariables;
        }

        public HttpHeaders getHeaders() {
            return headers;
        }

        public Map<String,Object> getUriParameters() {
            return uriParameters;
        }

        public <T> Optional<T> getBody() {
            try {
                return Optional.ofNullable((T)body);
            } catch(Exception e) {
                return Optional.empty();
            }
        }

        public Optional<List<String>> getPathVariables() {
            return Optional.ofNullable(pathVariables);
        }
    }
}
