package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientRequest;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponse;
import com.clinica.agendamento_consulta_medica.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private  final  PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/save")
    public ResponseEntity<PatientResponse> createPatient(@Valid  @RequestBody PatientRequest patientRequestDTO) {
        PatientResponse patient = patientService.registerPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @GetMapping("/getAllPatient")
    public ResponseEntity<List<PatientResponse>> getAllPatient() {
        return ResponseEntity.ok().body(patientService.findAll());
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getOnePatient(@PathVariable Long patientId, Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok().body(patientService.findById(patientId,authentication));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteOnePatient(@PathVariable Long patientId, Authentication authentication) throws AccessDeniedException {
        patientService.deleteById(patientId,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponse> updateOnePatient(@PathVariable Long patientId, @Valid @RequestBody PatientRequest patient,Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok().body(patientService.update(patientId, patient,authentication));
    }
}
