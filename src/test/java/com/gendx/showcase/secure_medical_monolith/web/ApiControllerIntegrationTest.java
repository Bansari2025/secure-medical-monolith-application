package com.gendx.showcase.secure_medical_monolith.web;

import com.gendx.showcase.secure_medical_monolith.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// These annotations set up a full, but simulated application environment for testing
@SpringBootTest // Loads the entire Spring application context, just like running the app
@AutoConfigureMockMvc //Configures and injects the MockMvc object
class ApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; //Tool for simulating HTTP requests

    @Autowired
    private JwtService jwtService; //Need this to generate valid tokens for tests

    @Test
    void getPatientData_whenUnauthenticated_shouldReturnForbidden()
        throws Exception {
        //Simulate a GET request to our protected endpoint without an authorisation header
        mockMvc.perform(get("/api/patients/123"))
                .andExpect(status().isForbidden()); // We assert that the HTTP status is 403 Forbidden.
    }

    @Test
    void getPatientData_withValidDoctorToken_shouldSucceed()
        throws Exception {
        // Create a valid token for a user with the DOCTOR role.
        String doctorToken = jwtService.generateToken("test-doctor", "ROLE_DOCTOR");

        System.out.println("DoctorToken " + doctorToken);

        mockMvc.perform(get("/api/patients/123")
                .header("Authorization", "Bearer " + doctorToken))
                .andExpect(status().isOk()) // We expect a 200 OK status
                .andExpect(jsonPath("$.patientId").value("123"));
    }

    @Test
    void getPatientData_withValidResearcherToken_shouldReturnForbidden()
        throws Exception {
        //Create a valid token for a user with the RESEARCHER role
        String researcherToken = jwtService.generateToken("test-researcher", "ROLE_RESEARCHER");

        //Attempt to access a DOCTOR-only endpoint
        mockMvc.perform(get("/api/patients/123")
                .header("Authorisation", "Bearer " + researcherToken))
                .andExpect(status().isForbidden()); // Assert that access is denied, proving @PreAuthorize works

    }

    @Test
    void getResultData_withValidResearcherToken_shouldSucceed()
        throws Exception {
        // Researcher can access the endpoints they are allowed to
        String researcherToken = jwtService.generateToken("test-researcher", "ROLE_RESEARCHER");

        mockMvc.perform(get("/api/results/456")
                .header("Authorization", "Bearer " + researcherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultId").value("456"));
    }
}