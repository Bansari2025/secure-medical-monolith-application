package com.gendx.showcase.secure_medical_monolith.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PatientService {

    public Map<String, String> getPatientById(String id) {

        // In a real app, this would fetch data from a database.
        // For now the return value is hardcoded to show proof of concept.
        return Map.of("patientId", id, "name", "Jane Doe - (Protected Patient Data)");
    }
}
