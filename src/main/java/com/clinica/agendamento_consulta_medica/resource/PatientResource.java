package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientDto;
import com.clinica.agendamento_consulta_medica.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/patient")
public class PatientResource {


    private  final  PatientService patientService;

    public PatientResource(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDto> save(@RequestBody PatientDto patient) {
        patient = patientService.save(patient);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(patient.getPatientId()).toUri();
        return ResponseEntity.created(uri).body(patient);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> findAll() {
        List<PatientDto> patient = patientService.findAll();
        return ResponseEntity.ok().body(patient);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientDto> findById(@PathVariable Long id) {
        PatientDto patient = patientService.findById(id);
        return ResponseEntity.ok().body(patient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientDto> update(@PathVariable Long id, @RequestBody PatientDto patient) {
        PatientDto patient1 = patientService.update(id, patient);
        return ResponseEntity.ok().body(patient1);
    }
}
