package com.gym.model.ai;

/**
 * Model cho request từ client đến AI chatbot
 */
public class ChatRequest {
    private String message;
    
    public ChatRequest() {}
    
    public ChatRequest(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                '}';
    }
}
