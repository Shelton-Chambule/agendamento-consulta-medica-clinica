package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequest;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping( "/consultations")
public class ConsultationController {

    private  final  ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/save")
    public ResponseEntity<ConsultationResponse>  createConsultation(@Valid @RequestBody ConsultationRequest consultation,
                                                                    Authentication authentication){
        log.info("Receiving request to create consultation: {}", consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationService.save(consultation, authentication));
    }

    @PostMapping("/{consultationId}/process")
    public ResponseEntity<ConsultationResponse> processConsultation(@PathVariable Long consultationId,
                                                                    Authentication authentication){
        log.info("Receiving request to process consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(consultationService.processConsultation(consultationId, authentication));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ConsultationResponse>> getAllConsultation(){
        log.info("Receiving request to list all consultations");
        return ResponseEntity.ok().body(consultationService.findAll());
    }

    @GetMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> getOneConsultation(@PathVariable Long consultationId,
                                                                   Authentication authentication){
        log.info("Receiving request to fetch consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(consultationService.findById(consultationId, authentication));
    }

    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Void> deleteOneConsultation(@PathVariable Long consultationId, Authentication authentication){
        log.info("Receiving request to delete consultation with id: {}", consultationId);
        consultationService.deleteById(consultationId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> updateConsultation(@PathVariable Long consultationId,
                                                                   @Valid @RequestBody ConsultationRequest consultation,
                                                                   Authentication authentication){
        log.info("Receiving request to update consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(consultationService.update(consultationId, consultation, authentication));
    }
}