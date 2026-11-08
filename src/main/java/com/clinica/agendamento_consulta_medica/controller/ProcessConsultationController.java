package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.service.ProcessConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessConsultationController {

    private final ProcessConsultationService process;

    @PostMapping("/{consultationId}/waiting")
    public ResponseEntity<ConsultationResponse> processConsultationWaiting(@PathVariable Long consultationId, Authentication authentication){
        log.info("Receiving request to process consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(process.processConsultationWaiting(consultationId, authentication));
    }

    @PostMapping("/{consultationId}/confirmed")
    public ResponseEntity<ConsultationResponse> processConsultationConfirmed(@PathVariable Long consultationId, Authentication authentication){
        log.info("Receiving request to process consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(process.processConsultationConfirmed(consultationId, authentication));
    }

    @PostMapping("/{consultationId}/scheduled")
    public ResponseEntity<ConsultationResponse> processConsultationScheduled(@PathVariable Long consultationId, Authentication authentication){
        log.info("Receiving request to process consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(process.processConsultationScheduled(consultationId, authentication));
    }

    @PostMapping("/{consultationId}/canceled")
    public ResponseEntity<ConsultationResponse> processConsultationCanceled(@PathVariable Long consultationId, Authentication authentication){
        log.info("Receiving request to process consultation with id: {}", consultationId);
        return ResponseEntity.ok().body(process.processConsultationCanceled(consultationId, authentication));
    }
}
