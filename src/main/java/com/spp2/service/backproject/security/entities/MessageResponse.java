package com.spp2.service.backproject.security.entities;

public class MessageResponse {

    private boolean successfully;

    private String message;

    public MessageResponse(String message, boolean successfully) {
        this.message = message;
        this.successfully = successfully;
    }

    public boolean isSuccessfully() {
        return successfully;
    }

    public void setSuccessfully(boolean successfully) {
        this.successfully = successfully;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
