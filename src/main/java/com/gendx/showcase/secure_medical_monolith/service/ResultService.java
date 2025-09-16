package com.gendx.showcase.secure_medical_monolith.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResultService {

    // In a real app, this would fetch from a database.
    // For now the return value is hardcoded to show proof of concept.
    public Map<String, String> getResultById(String id) {
        return Map.of("resultId", id, "value", "POSITIVE - (Protected Lab Data)");
    }
}
