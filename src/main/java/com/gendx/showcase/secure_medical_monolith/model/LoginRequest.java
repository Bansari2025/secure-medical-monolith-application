package com.gendx.showcase.secure_medical_monolith.model;

// A Java Record is a concise way to create a simple data carrier class
public record LoginRequest(String username, String password) {
}
