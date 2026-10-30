package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequest;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponse;
import com.clinica.agendamento_consulta_medica.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/specialtys")
public class SpecialtyController {

    private  final  SpecialtyService especialtyService;

    public SpecialtyController(SpecialtyService especialtyService) {
        this.especialtyService = especialtyService;
    }

    @PostMapping("/save")
    public ResponseEntity<SpecialtyResponse> save(@Valid @RequestBody SpecialtyRequest specialty){
        log.info("Receiving request to save specialty: {}", specialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(especialtyService.save(specialty));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialty(){
        log.info("Receiving request to list all specialties");
        return ResponseEntity.ok().body(especialtyService.findAll());
    }

    @GetMapping("/{specialtyId}")
    public ResponseEntity<SpecialtyResponse> getOneSpecialty(@PathVariable Long specialtyId){
        log.info("Receiving request to fetch specialty with id: {}", specialtyId);
        return ResponseEntity.ok().body(especialtyService.findById(specialtyId));
    }

    @DeleteMapping("/{specialtyId}")
    public  ResponseEntity<Void> deleteOneSpecialty(@PathVariable Long specialtyId,Authentication authentication){
        log.info("Receiving request to delete specialty with id: {}", specialtyId);
        especialtyService.deleteById(specialtyId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{specialtyId}")
    public  ResponseEntity<SpecialtyResponse> updateSpecialty(@PathVariable Long specialtyId,
                                                              @Valid @RequestBody SpecialtyRequest specialty,
                                                              Authentication authentication){
        log.info("Receiving request to update specialty with id: {}", specialtyId);
        return ResponseEntity.ok().body(especialtyService.update(specialtyId, specialty, authentication));
    }

}