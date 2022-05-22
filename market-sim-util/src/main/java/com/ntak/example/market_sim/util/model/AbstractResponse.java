package com.ntak.example.market_sim.util.model;

import java.util.Optional;

public abstract class AbstractResponse {
    protected ErrorMessage message;

    public boolean isError() {
        return message != null;
    }

    public Optional<ErrorMessage> getMessage() {
        return Optional.ofNullable(message);
    }

    public void setErrorMessage(String errorCode, String errorMessage) {
        this.message = new ErrorMessage(errorCode, errorMessage);
    }

    protected static class ErrorMessage {
        private final String errorCode;
        private final String message;

        public ErrorMessage(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
    }
}
