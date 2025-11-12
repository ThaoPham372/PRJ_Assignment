package model.ai;

public class GeminiRequest {
    public RequestPayload.Content[] contents;

    public GeminiRequest(RequestPayload.Content[] contents) {
        this.contents = contents;
    }
}