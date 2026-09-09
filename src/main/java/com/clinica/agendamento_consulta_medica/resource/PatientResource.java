package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponseDTO;
import com.clinica.agendamento_consulta_medica.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/patient")
public class PatientResource {

    private  final  PatientService patientService;

    public PatientResource(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PatientResponseDTO> save( @Valid  @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patient = patientService.save(patientRequestDTO);
        return ResponseEntity.ok().body(patient);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PatientResponseDTO>> findAll() {
        List<PatientResponseDTO> patient = patientService.findAll();
        return ResponseEntity.ok().body(patient);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable Long id) {
        PatientResponseDTO patient = patientService.findById(id);
        return ResponseEntity.ok().body(patient);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientRequestDTO> update(@PathVariable Long id, @RequestBody PatientRequestDTO patient) {
        PatientRequestDTO patient1 = patientService.update(id, patient);
        return ResponseEntity.ok().body(patient1);
    }
}
