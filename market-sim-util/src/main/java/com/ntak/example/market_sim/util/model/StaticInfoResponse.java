package com.ntak.example.market_sim.util.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StaticInfoResponse extends AbstractResponse {
    private String identifier;
    private Map<String,Object> parameters = new HashMap<>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String,Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public void addParameter(String parameter, Object value) {
        if (parameter != null) {
            parameters.put(parameter, value);
        }
    }
}
