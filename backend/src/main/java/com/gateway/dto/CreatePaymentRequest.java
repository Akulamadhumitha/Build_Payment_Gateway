package com.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePaymentRequest {

    @JsonProperty("order_id")
    private String orderId;

    private String method;
    private String vpa;
    private Card card;

    // getters & setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    // 🔽 Card inner class
    public static class Card {

        private String number;

        @JsonProperty("expiry_month")
        private String expiryMonth;

        @JsonProperty("expiry_year")
        private String expiryYear;

        private String cvv;

        @JsonProperty("holder_name")
        private String holderName;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getExpiryMonth() {
            return expiryMonth;
        }

        public void setExpiryMonth(String expiryMonth) {
            this.expiryMonth = expiryMonth;
        }

        public String getExpiryYear() {
            return expiryYear;
        }

        public void setExpiryYear(String expiryYear) {
            this.expiryYear = expiryYear;
        }

        public String getCvv() {
            return cvv;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }
    }
}
