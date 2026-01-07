package com.gateway.dto;

public class ErrorResponse {
    private Error error;

    public ErrorResponse(String code, String description){
        this.error = new Error(code, description);
    }

    public static class Error {
        private String code;
        private String description;

        public Error(String code, String description){
            this.code = code;
            this.description = description;
        }

        // Getters
    }

    // Getter for error
    public Error getError() { return error; }
}
