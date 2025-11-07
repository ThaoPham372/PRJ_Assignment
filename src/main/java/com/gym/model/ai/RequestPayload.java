package com.gym.model.ai;

public class RequestPayload {

    private String message;

    public String getMessage() {
        return message;
    }

    public static class Content {
        public Part[] parts;
        public Content(Part[] p) { this.parts = p; }
    }

    public static class Part {
        public String text;
        public Part(String t) { this.text = t; }
    }
}