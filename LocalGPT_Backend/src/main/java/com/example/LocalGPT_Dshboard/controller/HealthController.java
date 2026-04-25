package com.example.LocalGPT_Dshboard.controller;


import com.example.LocalGPT_Dshboard.model.HealthResponse;
import com.example.LocalGPT_Dshboard.service.OllamaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {

    private final OllamaService ollamaService;

    @Value("${ollama.model}")
    private String model;

    public HealthController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping("/health")
    public HealthResponse health() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMb = (totalMemory - freeMemory) / (1024 * 1024);
        long totalMb = runtime.maxMemory() / (1024 * 1024);

        boolean ollamaUp = ollamaService.isOllamaReachable();

        return new HealthResponse(
            ollamaUp ? "UP" : "DEGRADED",
            usedMb,
            totalMb,
            ollamaUp,
            model
        );
    }
}