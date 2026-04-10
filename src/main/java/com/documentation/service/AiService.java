package com.documentation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    private final WebClient webClient = WebClient.create();

    public String generate(String prompt) {
        String instruction = "You are a document writing assistant. Write well-structured document content based on the user's prompt. Return HTML formatted content.\n\n" + prompt;
        return callHuggingFace(instruction);
    }

    public String summarize(String content) {
        // Strip HTML and SVG tags, then collapse whitespace
        String plainText = content
                .replaceAll("<svg[^>]*>[\\s\\S]*?</svg>", "") // remove SVG blocks entirely
                .replaceAll("<[^>]+>", " ")                   // strip remaining HTML tags
                .replaceAll("&nbsp;", " ")
                .replaceAll("&[a-z]+;", "")                   // strip other HTML entities
                .replaceAll("\\s+", " ")
                .trim();

        if (plainText.isEmpty()) {
            return "No text content found in the document to summarize.";
        }

        // Truncate to ~3000 chars to stay within free-tier TPM limits
        String truncated = plainText.length() > 3000 ? plainText.substring(0, 3000) + "..." : plainText;
        String instruction = "You are a document summarization assistant. Summarize the following document content concisely.\n\n" + truncated;
        return callHuggingFace(instruction);
    }

    public Object extractRow(Map<String, Object> body) {
        int rowIndex = (int) body.get("rowIndex");
        List<List<String>> tableData = (List<List<String>>) body.get("tableData");
        if (rowIndex < 0 || rowIndex >= tableData.size()) {
            throw new RuntimeException("Invalid row index");
        }
        return tableData.get(rowIndex);
    }

    public Object extractColumn(Map<String, Object> body) {
        int colIndex = (int) body.get("colIndex");
        List<List<String>> tableData = (List<List<String>>) body.get("tableData");
        List<String> column = new java.util.ArrayList<>();
        for (List<String> row : tableData) {
            column.add(colIndex < row.size() ? row.get(colIndex) : "");
        }
        return column;
    }

    private String callHuggingFace(String prompt) {
        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            ),
            "max_tokens", 512
        );

        Map<String, Object> response = webClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(status -> status.isError(), clientResponse ->
                clientResponse.bodyToMono(String.class)
                    .map(errorBody -> new RuntimeException("Groq API error: " + clientResponse.statusCode() + " - " + errorBody))
            )
            .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

        if (response == null || !response.containsKey("choices")) {
            throw new RuntimeException("Unexpected response from Groq API");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message.get("content").toString();
    }
}
