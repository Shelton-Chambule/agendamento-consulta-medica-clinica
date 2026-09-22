package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequest;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/doctor")
public class DoctorController {

    private  final  DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/save/doctor")
    public ResponseEntity<DoctorResponse> save(@Valid  @RequestBody DoctorRequest doctor){
        DoctorResponse doctors = doctorService.registerDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctors);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DoctorResponse>> findAll(){
        List<DoctorResponse> doctor = doctorService.findAll();
        return ResponseEntity.ok().body(doctor);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> findById(@PathVariable Long doctorId){
        DoctorResponse doctor = doctorService.findById(doctorId);
        return ResponseEntity.ok().body(doctor);
    }

    @DeleteMapping("/{doctorId}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long doctorId){
        doctorService.deleteById(doctorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> update(@PathVariable Long doctorId, @Valid @RequestBody DoctorRequest doctor){
        DoctorResponse doctor1 = doctorService.update(doctorId,doctor);
        return ResponseEntity.ok().body(doctor1);
    }

}
