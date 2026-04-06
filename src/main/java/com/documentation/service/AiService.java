package com.documentation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final WebClient webClient = WebClient.create();

    public String generate(String prompt) {
        return callOpenAI("You are a document writing assistant. Write well-structured document content based on the user's prompt. Return HTML formatted content.", prompt);
    }

    public String summarize(String content) {
        return callOpenAI("You are a document summarization assistant. Summarize the following document content concisely.", content);
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

    private String callOpenAI(String systemMessage, String userMessage) {
        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", systemMessage + "\n\n" + userMessage)
                ))
            )
        );

        Map response = webClient.post()
            .uri(apiUrl + "?key=" + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(body)
            .retrieve()
            .onStatus(status -> status.isError(), clientResponse ->
                clientResponse.bodyToMono(String.class)
                    .map(errorBody -> new RuntimeException("AI API error: " + clientResponse.statusCode() + " - " + errorBody))
            )
            .bodyToMono(Map.class)
            .block();

        if (response == null || !response.containsKey("candidates")) {
            throw new RuntimeException("Unexpected response from Gemini API");
        }

        List<Map> candidates = (List<Map>) response.get("candidates");
        Map content = (Map) candidates.get(0).get("content");
        List<Map> parts = (List<Map>) content.get("parts");
        return (String) parts.get(0).get("text");
    }
}
