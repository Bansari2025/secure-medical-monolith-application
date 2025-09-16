package com.gendx.showcase.secure_medical_monolith.web;

import com.gendx.showcase.secure_medical_monolith.service.PatientService;
import com.gendx.showcase.secure_medical_monolith.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for protected API endpoints.
 * All endpoints under the "/api" path prefix are handled by this controller.
 * Access to these methods is protected by method-level security annotations.
 */
@RestController
@RequestMapping ("/api")
public class ApiController {

    private final PatientService patientService;
    private final ResultService resultService;

    /**
     * Injects the required service dependencies using constructor injection.
     */
    @Autowired
    public ApiController(PatientService patientService, ResultService resultService) {
        this.patientService = patientService;
        this.resultService = resultService;
    }

    /**
     * Endpoint to retrieve patient data.
     * Access is restricted to users with the 'DOCTOR' role.
     * The @PreAuthorize annotation is enforced by Spring Security *before* this method is executed.
     *  @param id The ID of the patient to retrieve.
     *  @return A map containing the patient data.
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public Map<String, String> getPatientData (@PathVariable String id) {

        System.out.println("---API LAYER: Request for patient data received---");
        return patientService.getPatientById(id);
    }

    /**
    * Endpoint to retrieve lab result data.
    * Access is restricted to users with either the 'DOCTOR' or 'RESEARCHER' role.
    */
    @GetMapping("/results/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'RESEARCHER')")
    public Map<String, String> getResultData(@PathVariable String id) {
        System.out.println("---API LAYER: Request for result data received ---");
        return resultService.getResultById(id);
    }
}
