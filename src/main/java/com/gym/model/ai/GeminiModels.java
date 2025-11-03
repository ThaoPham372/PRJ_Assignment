package com.gym.model.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model classes để handle Gemini API request/response
 */
public class GeminiModels {
    
    /**
     * Request payload cho Gemini API
     */
    public static class GeminiRequest {
        private Content[] contents;
        
        // Default constructor for Jackson
        public GeminiRequest() {}
        
        public GeminiRequest(Content[] contents) {
            this.contents = contents;
        }
        
        public Content[] getContents() {
            return contents;
        }
        
        public void setContents(Content[] contents) {
            this.contents = contents;
        }
    }
    
    /**
     * Content wrapper cho parts
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private Part[] parts;
        private String role; // Add role field for response
        
        // Default constructor for Jackson
        public Content() {}
        
        public Content(Part[] parts) {
            this.parts = parts;
        }
        
        public Part[] getParts() {
            return parts;
        }
        
        public void setParts(Part[] parts) {
            this.parts = parts;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
    }
    
    /**
     * Text part của content
     */
    public static class Part {
        private String text;
        
        // Default constructor for Jackson
        public Part() {}
        
        public Part(String text) {
            this.text = text;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
    }
    
    /**
     * Response từ Gemini API
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeminiResponse {
        private Candidate[] candidates;
        
        // Default constructor for Jackson
        public GeminiResponse() {}
        
        public Candidate[] getCandidates() {
            return candidates;
        }
        
        public void setCandidates(Candidate[] candidates) {
            this.candidates = candidates;
        }
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Candidate {
            private Content content;
            private String finishReason; // Add finishReason field for response
            
            // Default constructor for Jackson
            public Candidate() {}
            
            public Content getContent() {
                return content;
            }
            
            public void setContent(Content content) {
                this.content = content;
            }
            
            public String getFinishReason() {
                return finishReason;
            }
            
            public void setFinishReason(String finishReason) {
                this.finishReason = finishReason;
            }
        }
    }
}
