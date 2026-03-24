package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final String URL = "https://api.groq.com/openai/v1/chat/completions";

    // ✅ GENERATE QUESTIONS
    public String generateQuestions(String domain, String language, int count){

        String prompt;

        // 🔥 DIFFERENT PROMPTS FOR MCQ & CODING
        if(domain.equalsIgnoreCase("mcq")){

            prompt = """
            Generate %d MCQ questions for %s.

            FORMAT:
            [
              {
                "question": "string",
                "options": ["A","B","C","D"],
                "correctAnswer": "A"
              }
            ]

            RULES:
            - Always 4 options
            - One correct answer only
            - RETURN ONLY JSON ARRAY
            - NO explanation text outside JSON
            """.formatted(count, language);

        } else {

            prompt = """
            Generate %d CODING questions for %s.

            FORMAT:
            [
              {
                "question": "Problem statement ONLY",
                "input": "Example input ONLY (no code)",
                "output": "Expected output ONLY",
                "explanation": "Short logic explanation (no full code)"
              }
            ]

            STRICT RULES:
            - DO NOT include solution code
            - DO NOT include code inside input
            - Keep it clean problem statement
            - RETURN ONLY JSON ARRAY
            """.formatted(count, language);
        }

        return callGroq(prompt);
    }

    // ✅ EVALUATE CODE
    public String evaluateCode(String question, String userCode){

        String prompt = """
        Evaluate the following code strictly.

        Question:
        %s

        Code:
        %s

        RETURN ONLY JSON (NO EXTRA TEXT):
        {
          "score": 1 or 0,
          "feedback": "short explanation"
        }
        """.formatted(question, userCode);

        return callGroq(prompt);
    }

    // ✅ SAFE GROQ CALL
    private String callGroq(String prompt){

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> request = new HashMap<>();
        request.put("model", "llama-3.1-8b-instant");

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", prompt);

        messages.add(msg);
        request.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}