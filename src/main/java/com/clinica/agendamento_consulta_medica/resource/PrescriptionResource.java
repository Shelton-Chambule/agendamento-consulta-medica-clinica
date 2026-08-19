package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionDto;
import com.clinica.agendamento_consulta_medica.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/prescription" )
public class PrescriptionResource {


    private final PrescriptionService prescriptionService;

    public PrescriptionResource(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionDto> save(@RequestBody PrescriptionDto prescriptionDto){
        PrescriptionDto prescriptions = prescriptionService.save(prescriptionDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prescriptionDto.getId()).toUri();
        return ResponseEntity.created(uri).body(prescriptions);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> findAll(){
        List<PrescriptionDto> prescriptionDto = prescriptionService.findAll();
        return ResponseEntity.ok().body(prescriptionDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PrescriptionDto> findById(@PathVariable Long id){
        PrescriptionDto prescriptionDto = prescriptionService.findById(id);
        return ResponseEntity.ok().body(prescriptionDto);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        prescriptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public  ResponseEntity<PrescriptionDto> update(@RequestBody PrescriptionDto prescriptionDto,@PathVariable Long id ){
        PrescriptionDto prescriptionDto1 = prescriptionService.update(prescriptionDto,id);
        return ResponseEntity.ok().body(prescriptionDto1);
    }
}
