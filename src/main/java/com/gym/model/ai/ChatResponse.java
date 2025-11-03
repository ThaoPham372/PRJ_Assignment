package com.gym.model.ai;

/**
 * Model cho response từ AI chatbot về client
 */
public class ChatResponse {
    private String reply;
    private boolean success;
    private String error;
    
    public ChatResponse() {}
    
    public ChatResponse(String reply) {
        this.reply = reply;
        this.success = true;
    }
    
    public ChatResponse(String reply, boolean success) {
        this.reply = reply;
        this.success = success;
    }
    
    public static ChatResponse error(String errorMessage) {
        ChatResponse response = new ChatResponse();
        response.setError(errorMessage);
        response.setSuccess(false);
        return response;
    }
    
    public static ChatResponse success(String reply) {
        return new ChatResponse(reply, true);
    }
    
    public String getReply() {
        return reply;
    }
    
    public void setReply(String reply) {
        this.reply = reply;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    @Override
    public String toString() {
        return "ChatResponse{" +
                "reply='" + reply + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                '}';
    }
}
