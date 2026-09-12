package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponseDTO;
import com.clinica.agendamento_consulta_medica.service.SpecialtyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/specialty")
public class SpecialtyResource {

    private  final  SpecialtyService especialtyService;

    public SpecialtyResource(SpecialtyService especialtyService) {
        this.especialtyService = especialtyService;
    }

    @PostMapping("/save/specialty")
    public ResponseEntity<SpecialtyResponseDTO> save( @Valid @RequestBody SpecialtyRequestDTO specialty){
        SpecialtyResponseDTO specialtys = especialtyService.save(specialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtys);
    }

    @GetMapping("/find/specialty")
    public ResponseEntity<List<SpecialtyResponseDTO>> findAll(){
        List<SpecialtyResponseDTO> specialty = especialtyService.findAll();
        return ResponseEntity.ok().body(specialty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> findById(@PathVariable Long id){
        SpecialtyResponseDTO specialty = especialtyService.findById(id);
        return ResponseEntity.ok().body(specialty);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        especialtyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<SpecialtyResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SpecialtyRequestDTO specialty){
        SpecialtyResponseDTO specialty1 = especialtyService.update(id,specialty);
        return ResponseEntity.ok().body(specialty1);
    }

}
