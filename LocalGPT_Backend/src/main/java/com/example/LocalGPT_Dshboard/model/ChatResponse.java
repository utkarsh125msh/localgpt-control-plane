package com.example.LocalGPT_Dshboard.model;

public class ChatResponse {
    private String response;
    private long processingTimeMs;
    private int estimatedTokens;
    private double costSavedVsGpt4;

    public ChatResponse(String response, long processingTimeMs) {
        this.response = response;
        this.processingTimeMs = processingTimeMs;
        // Rough token estimate: ~0.75 tokens per word
        this.estimatedTokens = (int)(response.split("\\s+").length * 0.75 * 1.3);
        // GPT-4o costs ~$0.005 per 1K tokens (output)
        this.costSavedVsGpt4 = (estimatedTokens / 1000.0) * 0.005;
    }

    public String getResponse() { return response; }
    public long getProcessingTimeMs() { return processingTimeMs; }
    public int getEstimatedTokens() { return estimatedTokens; }
    public double getCostSavedVsGpt4() { return costSavedVsGpt4; }
}