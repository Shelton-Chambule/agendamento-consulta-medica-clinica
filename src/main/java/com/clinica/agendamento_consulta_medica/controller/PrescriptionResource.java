package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponseDTO;
import com.clinica.agendamento_consulta_medica.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/prescription" )
public class PrescriptionResource {

    private final PrescriptionService prescriptionService;

    public PrescriptionResource(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save/prescription")
    public ResponseEntity<PrescriptionResponseDTO> save(@Valid  @RequestBody PrescriptionRequestDTO prescriptionRequestDTO){
        PrescriptionResponseDTO prescriptions = prescriptionService.save(prescriptionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptions);
    }

    @GetMapping("/findAll/prescription")
    public ResponseEntity<List<PrescriptionResponseDTO>> findAll(){
        List<PrescriptionResponseDTO> prescriptionDto = prescriptionService.findAll();
        return ResponseEntity.ok().body(prescriptionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> findById(@PathVariable Long id){
        PrescriptionResponseDTO prescriptionDto = prescriptionService.findById(id);
        return ResponseEntity.ok().body(prescriptionDto);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        prescriptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<PrescriptionResponseDTO> update(@Valid @RequestBody PrescriptionResponseDTO prescriptionDto, @PathVariable Long id ){
        PrescriptionResponseDTO prescriptionDto1 = prescriptionService.update(prescriptionDto,id);
        return ResponseEntity.ok().body(prescriptionDto1);
    }
}
