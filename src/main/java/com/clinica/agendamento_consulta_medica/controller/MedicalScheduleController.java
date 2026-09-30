package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.service.MedicalScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/medicalschedules")
public class MedicalScheduleController {

    private  final  MedicalScheduleService medicalScheduleService;

    public MedicalScheduleController(MedicalScheduleService medicalScheduleService) {
        this.medicalScheduleService = medicalScheduleService;
    }

    @PostMapping("/save")
    public ResponseEntity<MedicalSheduleResponse> save (@Valid @RequestBody MedicalScheduleRequest medicalSchedule, Authentication authentication){
        MedicalSheduleResponse medicalSchedules = medicalScheduleService.save(medicalSchedule,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalSchedules);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<MedicalSheduleResponse>> getAllMedicalSchedule(){
        return ResponseEntity.ok().body( medicalScheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalSheduleResponse> getOneMedicalSchedule(@PathVariable Long id, Authentication authentication){
        return ResponseEntity.ok().body(medicalScheduleService.findById(id,authentication));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteOneMedicalSchedule(@PathVariable Long id,Authentication authentication){
        medicalScheduleService.deleteById(id , authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalSheduleResponse> updateMedicalSchedule(@PathVariable Long id, @Valid @RequestBody MedicalScheduleRequest medicalSchedule,Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(medicalScheduleService.update(id,medicalSchedule,authentication));
    }
}
