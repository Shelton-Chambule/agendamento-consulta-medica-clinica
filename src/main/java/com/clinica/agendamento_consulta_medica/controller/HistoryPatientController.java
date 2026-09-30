package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.service.HistoryPatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/historyConsultation")
public class HistoryPatientController {

    private final HistoryPatientService historyPatientService;

    public HistoryPatientController(HistoryPatientService historyPatientService) {
        this.historyPatientService = historyPatientService;
    }

    @PostMapping("/save/history")
    public ResponseEntity<HistoryPatientResponse> save(@Valid @RequestBody Consultation consultation){
        return ResponseEntity.status(HttpStatus.CREATED).body(historyPatientService.save(consultation));
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<HistoryPatientResponse>> getAllHistory() {
        List<HistoryPatientResponse> historyPatient = historyPatientService.findAll();
        return ResponseEntity.ok().body(historyPatient);
    }

    @GetMapping("/historyConsultation/{id}")
    public ResponseEntity<HistoryPatientResponse> getOneHistory(@PathVariable Long id) {
        return ResponseEntity.ok().body(historyPatientService.findById(id));
    }

    @DeleteMapping("/historyConsultation/{id}")
    public ResponseEntity<Void> deleteOneHistory(@PathVariable  Long id){
        historyPatientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
