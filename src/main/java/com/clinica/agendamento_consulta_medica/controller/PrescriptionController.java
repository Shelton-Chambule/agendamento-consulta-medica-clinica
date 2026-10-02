package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/prescriptions" )
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<PrescriptionResponse> createPrescription(@Valid @RequestBody PrescriptionRequest prescriptionRequestDTO,
                                                                    Authentication authentication){
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.save(prescriptionRequestDTO, authentication));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PrescriptionResponse>> getAllPrescription(){
        return ResponseEntity.ok().body(prescriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getOnePrescription(@PathVariable Long id, Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.findById(id, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOnePrescription(@PathVariable Long id, Authentication authentication){
        prescriptionService.deleteById(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> updatePrescription(@PathVariable Long id,
                                                                    @Valid @RequestBody PrescriptionRequest prescriptionDto,
                                                                    Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.update(id, prescriptionDto, authentication));
    }
}
