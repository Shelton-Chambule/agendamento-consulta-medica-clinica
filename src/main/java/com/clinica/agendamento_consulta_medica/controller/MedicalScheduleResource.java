package com.clinica.agendamento_consulta_medica.controller;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponseDTO;
import com.clinica.agendamento_consulta_medica.service.MedicalScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/medicalshedule")
public class MedicalScheduleResource {

    private  final  MedicalScheduleService medicalScheduleService;

    public MedicalScheduleResource(MedicalScheduleService medicalScheduleService) {
        this.medicalScheduleService = medicalScheduleService;
    }

    @PostMapping("/save")
    public ResponseEntity<MedicalSheduleResponseDTO> save ( @Valid @RequestBody MedicalScheduleRequestDTO medicalSchedule){
        MedicalSheduleResponseDTO medicalSchedules = medicalScheduleService.save(medicalSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalSchedules);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<MedicalSheduleResponseDTO>> findAll(){
        List<MedicalSheduleResponseDTO> medicalSchedules = medicalScheduleService.findAll();
        return ResponseEntity.ok().body(medicalSchedules);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MedicalSheduleResponseDTO> findById(@PathVariable Long id){
        MedicalSheduleResponseDTO medicalSchedule = medicalScheduleService.findById(id);
        return ResponseEntity.ok().body(medicalSchedule);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        medicalScheduleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MedicalSheduleResponseDTO> update(@PathVariable Long id,  @Valid @RequestBody MedicalScheduleRequestDTO medicalSchedule){
        MedicalSheduleResponseDTO medicalSchedule1  = medicalScheduleService.update(id,medicalSchedule);
        return ResponseEntity.ok().body(medicalSchedule1);
    }
}
