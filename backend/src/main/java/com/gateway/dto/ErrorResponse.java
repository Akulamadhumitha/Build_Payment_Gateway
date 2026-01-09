package com.gateway.dto;

public class ErrorResponse {

    private Error error;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.error = new Error(message);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    // Make inner class static so Jackson can serialize it
    public static class Error {
        private String message;

        public Error() {
        }

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
