package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientRequest;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponse;
import com.clinica.agendamento_consulta_medica.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private  final  PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/save/patient")
    public ResponseEntity<PatientResponse> save(@Valid  @RequestBody PatientRequest patientRequestDTO) {
        PatientResponse patient = patientService.registerPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @GetMapping("/findAllPatient")
    public ResponseEntity<List<PatientResponse>> findAll() {
        List<PatientResponse> patient = patientService.findAll();
        return ResponseEntity.ok().body(patient);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> findById(@PathVariable Long patientId) {
        PatientResponse patient = patientService.findById(patientId);
        return ResponseEntity.ok().body(patient);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> delete(@PathVariable Long patientId) {
        patientService.deleteById(patientId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{patientId}")
    public ResponseEntity<PatientResponse> update(@PathVariable Long patientId, @Valid @RequestBody PatientRequest patient) {
        PatientResponse patient1 = patientService.update(patientId, patient);
        return ResponseEntity.ok().body(patient1);
    }
}
