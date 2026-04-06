package com.documentation.controller;

import com.documentation.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestBody Map<String, String> body) {
        try {
            String content = aiService.generate(body.get("prompt"));
            return ResponseEntity.ok(Map.of("content", content));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarize(@RequestBody Map<String, String> body) {
        try {
            String summary = aiService.summarize(body.get("content"));
            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/table/extract-row")
    public ResponseEntity<Map<String, Object>> extractRow(@RequestBody Map<String, Object> body) {
        try {
            Object result = aiService.extractRow(body);
            return ResponseEntity.ok(Map.of("data", result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/table/extract-column")
    public ResponseEntity<Map<String, Object>> extractColumn(@RequestBody Map<String, Object> body) {
        try {
            Object result = aiService.extractColumn(body);
            return ResponseEntity.ok(Map.of("data", result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
