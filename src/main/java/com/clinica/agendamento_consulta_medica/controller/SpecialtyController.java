package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequest;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponse;
import com.clinica.agendamento_consulta_medica.service.SpecialtyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/specialty")
public class SpecialtyController {

    private  final  SpecialtyService especialtyService;

    public SpecialtyController(SpecialtyService especialtyService) {
        this.especialtyService = especialtyService;
    }

    @PostMapping("/save/specialty")
    public ResponseEntity<SpecialtyResponse> save(@Valid @RequestBody SpecialtyRequest specialty){
        SpecialtyResponse specialtys = especialtyService.save(specialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtys);
    }

    @GetMapping("/find/specialty")
    public ResponseEntity<List<SpecialtyResponse>> findAll(){
        List<SpecialtyResponse> specialty = especialtyService.findAll();
        return ResponseEntity.ok().body(specialty);
    }

    @GetMapping("/{specialtyId}")
    public ResponseEntity<SpecialtyResponse> findById(@PathVariable Long specialtyId){
        SpecialtyResponse specialty = especialtyService.findById(specialtyId);
        return ResponseEntity.ok().body(specialty);
    }

    @DeleteMapping("/{specialtyId}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long specialtyId){
        especialtyService.deleteById(specialtyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{specialtyId}")
    public  ResponseEntity<SpecialtyResponse> update(@PathVariable Long specialtyId, @Valid @RequestBody SpecialtyRequest specialty){
        SpecialtyResponse specialty1 = especialtyService.update(specialtyId,specialty);
        return ResponseEntity.ok().body(specialty1);
    }

}
