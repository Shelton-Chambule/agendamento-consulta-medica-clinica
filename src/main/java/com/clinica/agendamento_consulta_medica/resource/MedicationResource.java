package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.medication.MedicationDto;
import com.clinica.agendamento_consulta_medica.service.MedicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medication")
public class MedicationResource {


    private  final  MedicationService medicationService;

    public MedicationResource(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping
    public ResponseEntity<MedicationDto> save(@RequestBody MedicationDto medicationDto){
        MedicationDto medications = medicationService.save(medicationDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(medicationDto.getId()).toUri();
        return ResponseEntity.created(uri).body(medications);
    }

    @GetMapping
    public ResponseEntity<List<MedicationDto>> findAll(){
        List<MedicationDto>medicationDto = medicationService.findAll();
        return ResponseEntity.ok().body(medicationDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MedicationDto> findById(@PathVariable Long id){
        MedicationDto medicationDto = medicationService.findById(id);
        return ResponseEntity.ok().body(medicationDto);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        medicationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public  ResponseEntity<MedicationDto> update(@RequestBody MedicationDto medication,@PathVariable Long id ){
        MedicationDto medication1 = medicationService.update(medication,id);
        return ResponseEntity.ok().body(medication1);
    }
}
