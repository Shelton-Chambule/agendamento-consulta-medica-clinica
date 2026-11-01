package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.service.HistoryPatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping( "/historyConsultation")
public class HistoryPatientController {

    private final HistoryPatientService historyPatientService;

    public HistoryPatientController(HistoryPatientService historyPatientService) {
        this.historyPatientService = historyPatientService;
    }

    @PostMapping("/save/history")
    public ResponseEntity<HistoryPatientResponse> save(@Valid @RequestBody Consultation consultation){
        log.info("Receiving request to save consultation history: {}", consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(historyPatientService.save(consultation));
    }

    @GetMapping("/findAll")
    public ResponseEntity<Page<HistoryPatientResponse>> getAllHistory(Pageable pageable) {
        log.info("Receiving request to list all consultation history");
        return ResponseEntity.ok().body(historyPatientService.findAll(pageable));
    }

    @GetMapping("/historyConsultation/{id}")
    public ResponseEntity<HistoryPatientResponse> getOneHistory(@PathVariable Long id) {
        log.info("Receiving request to fetch history with id: {}", id);
        return ResponseEntity.ok().body(historyPatientService.findById(id));
    }

    @DeleteMapping("/historyConsultation/{id}")
    public ResponseEntity<Void> deleteOneHistory(@PathVariable  Long id){
        log.info("Receiving request to delete history with id: {}", id);
        historyPatientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}