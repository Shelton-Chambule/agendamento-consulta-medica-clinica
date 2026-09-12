package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponseDTO;
import com.clinica.agendamento_consulta_medica.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/doctor")
public class DoctorResource {

    private  final  DoctorService doctorService;

    public DoctorResource(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/save/doctor")
    public ResponseEntity<DoctorResponseDTO> save( @Valid  @RequestBody DoctorRequestDTO doctor){
        DoctorResponseDTO doctors = doctorService.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctors);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DoctorResponseDTO>> findAll(){
        List<DoctorResponseDTO> doctor = doctorService.findAll();
        return ResponseEntity.ok().body(doctor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable Long id){
        DoctorResponseDTO doctor = doctorService.findById(id);
        return ResponseEntity.ok().body(doctor);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> update(@PathVariable Long id,  @Valid @RequestBody DoctorRequestDTO doctor){
        DoctorResponseDTO doctor1 = doctorService.update(id,doctor);
        return ResponseEntity.ok().body(doctor1);
    }

}
