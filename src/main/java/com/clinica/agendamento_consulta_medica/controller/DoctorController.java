package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequest;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.service.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping( "/doctors")
public class DoctorController {

    private  final  DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/create")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid  @RequestBody DoctorRequest doctor){
        log.info("Receiving request to create doctor: {}", doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.registerDoctor(doctor));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorResponse>> getAllDoctor(){
        log.info("Receiving request to list all doctors");
        return ResponseEntity.ok().body(doctorService.findAll());
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getOneDoctor(@PathVariable Long doctorId, Authentication authentication) {
        log.info("Receiving request to fetch doctor with id: {}", doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.findById(doctorId,authentication));
    }

    @DeleteMapping("/{doctorId}")
    public  ResponseEntity<Void> deleteOneDoctor(@PathVariable Long doctorId, Authentication authentication){
        log.info("Receiving request to delete doctor with id: {}", doctorId);
        doctorService.deleteById(doctorId,authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody DoctorRequest doctor, Authentication authentication) {
        log.info("Receiving request to update doctor with id: {}", doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.update(doctorId,doctor,authentication));
    }

}