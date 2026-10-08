package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequest;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationService.save(consultation, authentication));
    }

    @PostMapping("/{consultationId}/process")
    public ResponseEntity<ConsultationResponse> processConsultation(@PathVariable Long consultationId,
                                                                     Authentication authentication){
        return ResponseEntity.ok().body(consultationService.processConsultation(consultationId, authentication));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ConsultationResponse>> getAllConsultation(){
        return ResponseEntity.ok().body(consultationService.findAll());
    }

    @GetMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> getOneConsultation(@PathVariable Long consultationId,
                                                                    Authentication authentication){
        return ResponseEntity.ok().body(consultationService.findById(consultationId, authentication));
    }

    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Void> deleteOneConsultation(@PathVariable Long consultationId, Authentication authentication){
        consultationService.deleteById(consultationId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> updateConsultation(@PathVariable Long consultationId,
                                                                    @Valid @RequestBody ConsultationRequest consultation,
                                                                    Authentication authentication){
        return ResponseEntity.ok().body(consultationService.update(consultationId, consultation, authentication));
    }
}
