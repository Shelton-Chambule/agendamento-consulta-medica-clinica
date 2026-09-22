package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/prescription" )
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save/prescription")
    public ResponseEntity<PrescriptionResponse> save(@Valid  @RequestBody PrescriptionRequest prescriptionRequestDTO){
        PrescriptionResponse prescriptions = prescriptionService.save(prescriptionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptions);
    }

    @GetMapping("/findAll/prescription")
    public ResponseEntity<List<PrescriptionResponse>> findAll(){
        List<PrescriptionResponse> prescriptionDto = prescriptionService.findAll();
        return ResponseEntity.ok().body(prescriptionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> findById(@PathVariable Long id){
        PrescriptionResponse prescriptionDto = prescriptionService.findById(id);
        return ResponseEntity.ok().body(prescriptionDto);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        prescriptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<PrescriptionResponse> update( @PathVariable Long id, @Valid @RequestBody PrescriptionRequest prescriptionDto ){
        PrescriptionResponse prescriptionDto1 = prescriptionService.update(id,prescriptionDto);
        return ResponseEntity.ok().body(prescriptionDto1);
    }
}
