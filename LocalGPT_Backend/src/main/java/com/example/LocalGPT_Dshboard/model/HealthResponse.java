package com.example.LocalGPT_Dshboard.model;

public class HealthResponse {
    private String status;
    private long ramUsedMb;
    private long ramTotalMb;
    private boolean ollamaRunning;
    private String model;

    public HealthResponse(String status, long ramUsedMb, long ramTotalMb,
                          boolean ollamaRunning, String model) {
        this.status = status;
        this.ramUsedMb = ramUsedMb;
        this.ramTotalMb = ramTotalMb;
        this.ollamaRunning = ollamaRunning;
        this.model = model;
    }

    public String getStatus() { return status; }
    public long getRamUsedMb() { return ramUsedMb; }
    public long getRamTotalMb() { return ramTotalMb; }
    public boolean isOllamaRunning() { return ollamaRunning; }
    public String getModel() { return model; }
}