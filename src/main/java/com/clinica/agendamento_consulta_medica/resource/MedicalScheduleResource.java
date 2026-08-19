package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleDto;
import com.clinica.agendamento_consulta_medica.service.MedicalScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping( "/medicalshedule")
public class MedicalScheduleResource {


    private  final  MedicalScheduleService medicalScheduleService;

    public MedicalScheduleResource(MedicalScheduleService medicalScheduleService) {
        this.medicalScheduleService = medicalScheduleService;
    }

    @PostMapping
    public ResponseEntity<MedicalScheduleDto> save (@RequestBody MedicalScheduleDto medicalSchedule){
        MedicalScheduleDto medicalSchedules = medicalScheduleService.save(medicalSchedule);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(medicalSchedule.getId()).toUri();
        return ResponseEntity.created(uri).body(medicalSchedules);
    }

    @GetMapping
    public ResponseEntity<List<MedicalScheduleDto>> findAll(){
        List<MedicalScheduleDto> medicalSchedules = medicalScheduleService.findAll();
        return ResponseEntity.ok().body(medicalSchedules);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MedicalScheduleDto> findById(@PathVariable Long id){
        MedicalScheduleDto medicalSchedule = medicalScheduleService.findById(id);
        return ResponseEntity.ok().body(medicalSchedule);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        medicalScheduleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MedicalScheduleDto> update(@PathVariable Long id, @RequestBody MedicalScheduleDto medicalSchedule){
        MedicalScheduleDto medicalSchedule1  = medicalScheduleService.update(id,medicalSchedule);
        return ResponseEntity.ok().body(medicalSchedule1);
    }
}
