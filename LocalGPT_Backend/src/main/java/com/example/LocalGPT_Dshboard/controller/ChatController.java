package com.example.LocalGPT_Dshboard.controller;

import com.example.LocalGPT_Dshboard.model.ChatRequest;
import com.example.LocalGPT_Dshboard.model.ChatResponse;
import com.example.LocalGPT_Dshboard.service.OllamaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final OllamaService ollamaService;

    public ChatController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        if (request.getPrompt() == null || request.getPrompt().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"prompt cannot be empty\"}");
        }

        log.info("[{}] POST /api/chat | prompt=\"{}\"",
                java.time.LocalDateTime.now(),
                request.getPrompt().substring(0, Math.min(50, request.getPrompt().length())));

        long start = System.currentTimeMillis();
        try {
            String llmResponse = ollamaService.generate(request.getPrompt());
            long elapsed = System.currentTimeMillis() - start;
            return ResponseEntity.ok(new ChatResponse(llmResponse, elapsed));
        } catch (Exception e) {
            log.error("Chat failed: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"LLM call failed: " + e.getMessage() + "\"}");
        }
    }
}