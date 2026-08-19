package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationDto;
import com.clinica.agendamento_consulta_medica.service.ConsultationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping( "/consultation")
public class ConsultationResource {

    private  final  ConsultationService consultationService;

    public ConsultationResource(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    public ResponseEntity<ConsultationDto>  save(@RequestBody ConsultationDto consultation){
        ConsultationDto consultations = consultationService.save(consultation);
        URI uri  = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(consultation.getId()).toUri();
        return ResponseEntity.created(uri).body(consultations);
    }

    @GetMapping
    public ResponseEntity<List<ConsultationDto>> findAll(){
        List<ConsultationDto> consultation = consultationService.findAll();
        return ResponseEntity.ok().body(consultation);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConsultationDto> findById(@PathVariable Long id){
        ConsultationDto consultation = consultationService.findById(id);
        return ResponseEntity.ok().body(consultation);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        consultationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ConsultationDto> update(@PathVariable Long id, @RequestBody ConsultationDto consultation){
        ConsultationDto consultation1 = consultationService.update(id, consultation);
        return ResponseEntity.ok().body(consultation1);
    }
}
