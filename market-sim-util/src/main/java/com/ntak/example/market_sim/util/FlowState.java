package com.ntak.example.market_sim.util;

import java.util.*;

public class FlowState {
    private final Map<String,Object> requests;
    private final Map<String,Object> responses;

    public FlowState() {
        this.requests = new LinkedHashMap<>();
        this.responses = new LinkedHashMap<>();
    }

    public void addRequest(String identifier, Object request) {
        if (request != null) {
            requests.put(identifier, request);
        }
    }

    public <T> Optional<T> getRequest(String identifier) {
        try {
            if (requests.containsKey(identifier)) {
                return Optional.ofNullable((T) requests.get(identifier));
            }
        } catch (ClassCastException e) {
        }

        return Optional.empty();
    }

    public <T> Optional<T> getRequest(int index) {
        try {
            Object request;
            if ((request = (new LinkedList(requests.values())).get(index)) != null) {
                return Optional.ofNullable((T) request);
            }
        } catch (ClassCastException e) {
        }

        return Optional.empty();
    }

    public void addResponse(String identifier, Object response) {
        if (response != null) {
            responses.put(identifier, response);
        }
    }

    public <T> Optional<T> getResponse(String identifier) {
        try {
            if (responses.containsKey(identifier)) {
                return Optional.ofNullable((T) responses.get(identifier));
            }
        } catch (ClassCastException e) {
        }

        return Optional.empty();
    }

    public <T> Optional<T> getResponse(int index) {
        try {
            Object response;
            if ((response = ((List<Object>)responses.values()).get(index)) != null) {
                return Optional.ofNullable((T) response);
            }
        } catch (ClassCastException e) {
        }

        return Optional.empty();
    }

    public int responseCount() {
        return responses.values().size();
    }
}
