package com.sentinelai.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIAnalysisService {

    private final ChatClient chatClient;

    @Cacheable(value = "aiAnalysisCache", key = "#root.target.hashStackTrace(#stackTrace)")
    public String analyzeException(String exceptionClass,
                                   String message,
                                   String stackTrace) {
        log.info("Cache MISS — sending exception to Groq for analysis...");

        String prompt = """
                You are a Senior Java Developer and SRE expert.
                Analyze this Java exception and respond with ONLY a JSON object.
                No extra text. No markdown. Just the JSON.
                
                Exception Class: %s
                Message: %s
                Stack Trace: %s
                
                Respond with this exact JSON format:
                {
                    "rootCause": "explain the root cause in one sentence",
                    "affectedComponent": "the class or method where it occurred",
                    "suggestedFix": "explain how to fix it in one sentence",
                    "severity": "CRITICAL or HIGH or MEDIUM or LOW"
                }
                """.formatted(exceptionClass, message, stackTrace);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("Groq analysis complete: {}", response);
        return response;
    }

    public String hashStackTrace(String stackTrace) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(stackTrace.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(stackTrace.hashCode());
        }
    }
}