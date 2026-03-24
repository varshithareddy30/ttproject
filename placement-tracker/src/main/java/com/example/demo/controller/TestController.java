package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.AiService;
import com.example.demo.model.Goal;
import com.example.demo.repository.GoalRepository;

import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins="http://localhost:3000")
public class TestController {

    @Autowired
    private AiService aiService;

    @Autowired
    private GoalRepository goalRepository;

    // ✅ GENERATE TEST
    @GetMapping("/{goalId}")
    public String generateTest(@PathVariable Long goalId){

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        String domain = goal.getType().equalsIgnoreCase("mcq") ? "MCQ" : "CODING";
        String language = goal.getTitle();
        int questionCount = goal.getTarget();

        return aiService.generateQuestions(domain, language, questionCount);
    }

    // ✅ EVALUATE CODE
    @PostMapping("/evaluate")
    public String evaluateCode(@RequestBody Map<String, String> data){

        String question = data.get("question");
        String code = data.get("code");

        return aiService.evaluateCode(question, code);
    }
}