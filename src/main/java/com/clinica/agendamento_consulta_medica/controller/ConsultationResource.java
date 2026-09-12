package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponseDTO;
import com.clinica.agendamento_consulta_medica.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/consultation")
public class ConsultationResource {

    private  final  ConsultationService consultationService;

    public ConsultationResource(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/save/consultation")
    public ResponseEntity<ConsultationResponseDTO>  save( @Valid  @RequestBody ConsultationRequestDTO consultation){
        ConsultationResponseDTO consultations = consultationService.save(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultations);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ConsultationResponseDTO>> findAll(){
        List<ConsultationResponseDTO> consultation = consultationService.findAll();
        return ResponseEntity.ok().body(consultation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> findById(@PathVariable Long id){
        ConsultationResponseDTO consultation = consultationService.findById(id);
        return ResponseEntity.ok().body(consultation);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        consultationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponseDTO> update(@PathVariable Long id,  @Valid @RequestBody ConsultationRequestDTO consultation){
        ConsultationResponseDTO consultation1 = consultationService.update(id, consultation);
        return ResponseEntity.ok().body(consultation1);
    }
}
