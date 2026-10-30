package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.service.MedicalScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping( "/medicalschedules")
public class MedicalScheduleController {

    private  final  MedicalScheduleService medicalScheduleService;

    public MedicalScheduleController(MedicalScheduleService medicalScheduleService) {
        this.medicalScheduleService = medicalScheduleService;
    }

    @PostMapping("/save")
    public ResponseEntity<MedicalSheduleResponse> save (@Valid @RequestBody MedicalScheduleRequest medicalSchedule, Authentication authentication){
        log.info("Receiving request to save medical schedule: {}", medicalSchedule);
        MedicalSheduleResponse medicalSchedules = medicalScheduleService.save(medicalSchedule,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalSchedules);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<MedicalSheduleResponse>> getAllMedicalSchedule(){
        log.info("Receiving request to list all medical schedules");
        return ResponseEntity.ok().body( medicalScheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalSheduleResponse> getOneMedicalSchedule(@PathVariable Long id){
        log.info("Receiving request to fetch medical schedule with id: {}", id);
        return ResponseEntity.ok().body(medicalScheduleService.findById(id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteOneMedicalSchedule(@PathVariable Long id,Authentication authentication){
        log.info("Receiving request to delete medical schedule with id: {}", id);
        medicalScheduleService.deleteById(id , authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalSheduleResponse> updateMedicalSchedule(@PathVariable Long id, @Valid @RequestBody MedicalScheduleRequest medicalSchedule,Authentication authentication){
        log.info("Receiving request to update medical schedule with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(medicalScheduleService.update(id,medicalSchedule,authentication));
    }
}