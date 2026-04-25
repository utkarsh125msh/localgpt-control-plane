package com.example.LocalGPT_Dshboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class OllamaService {

    private static final Logger log = LoggerFactory.getLogger(OllamaService.class);

    @Value("${ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${ollama.model}")
    private String model;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String generate(String prompt) throws Exception {
        // String requestBody = String.format(
        //     "{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}",
        //     model,
        //     prompt.replace("\"", "\\\"").replace("\n", "\\n")
        // );

        String safePrompt = prompt
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "");

        String requestBody = String.format( 
            "{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}",
            model,
            safePrompt
        );

        log.info("[{}] Sending prompt to Ollama | model={} | prompt_length={}",
                java.time.LocalDateTime.now(), model, prompt.length());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaBaseUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(120))
                .build();

        HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Ollama returned HTTP " + response.statusCode());
        }

        // Parse "response" field from JSON manually (no extra deps needed)
        String body = response.body();
        int start = body.indexOf("\"response\":\"") + 12;
        int end = body.indexOf("\",\"done\"");
        if (start < 12 || end < 0) {
            throw new RuntimeException("Unexpected Ollama response format: " + body);
        }

        String result = body.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");

        log.info("[{}] Ollama response received | length={}", 
                java.time.LocalDateTime.now(), result.length());

        return result;
    }

    public boolean isOllamaReachable() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaBaseUrl + "/api/tags"))
                    .GET()
                    .timeout(Duration.ofSeconds(3))
                    .build();
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            log.warn("Ollama health check failed: {}", e.getMessage());
            return false;
        }
    }
}