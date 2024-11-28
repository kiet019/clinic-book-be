package com.c1se62.clinic_booking.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {
    private String userMessage;

    // Constructor mặc định (khi không có constructor sẽ bị lỗi)
    @JsonCreator
    public ChatRequest(@JsonProperty("userMessage") String userMessage) {
        this.userMessage = userMessage;
    }

    // Getter và setter để Jackson có thể ánh xạ các trường
    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
