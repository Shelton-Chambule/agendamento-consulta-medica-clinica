package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.service.HistoryPatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/historyConsultation")
public class HistoryPatientResource {

    private final HistoryPatientService historyPatientService;

    public HistoryPatientResource(HistoryPatientService historyPatientService) {
        this.historyPatientService = historyPatientService;
    }

    @PostMapping("/save/history")
    public ResponseEntity<HistoryPatientResponseDTO> save(@Valid @RequestBody Consultation consultation){
        HistoryPatientResponseDTO historyPatientDto1 = historyPatientService.save(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(historyPatientDto1);
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<HistoryPatientResponseDTO>> findAll() {
        List<HistoryPatientResponseDTO> historyPatient = historyPatientService.findAll();
        return ResponseEntity.ok().body(historyPatient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoryPatientResponseDTO> findById(@PathVariable Long id) {
        HistoryPatientResponseDTO historyPatient = historyPatientService.findById(id);
        return ResponseEntity.ok().body(historyPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable  Long id){
        historyPatientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
