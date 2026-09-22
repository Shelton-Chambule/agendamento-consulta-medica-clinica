package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequest;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/consultation")
public class ConsultationController {

    private  final  ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/save/consultation")
    public ResponseEntity<ConsultationResponse>  save(@Valid  @RequestBody ConsultationRequest consultation){
        ConsultationResponse consultations = consultationService.save(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultations);
    }

    @PostMapping("/{consultationId}/process")
    public ResponseEntity<ConsultationResponse> process(@PathVariable Long consultationId){
        ConsultationResponse consultations = consultationService.processConsultation(consultationId);
        return ResponseEntity.ok().body(consultations);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ConsultationResponse>> findAll(){
        List<ConsultationResponse> consultation = consultationService.findAll();
        return ResponseEntity.ok().body(consultation);
    }

    @GetMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> findById(@PathVariable Long consultationId){
        ConsultationResponse consultation = consultationService.findById(consultationId);
        return ResponseEntity.ok().body(consultation);
    }

    @DeleteMapping("/{consultationId}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long consultationId){
        consultationService.deleteById(consultationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> update(@PathVariable Long consultationId, @Valid @RequestBody ConsultationRequest consultation){
        ConsultationResponse consultation1 = consultationService.update(consultationId, consultation);
        return ResponseEntity.ok().body(consultation1);
    }
}
