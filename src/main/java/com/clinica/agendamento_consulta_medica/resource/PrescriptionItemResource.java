package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.prescriptionItem.PrescriptionItemDto;
import com.clinica.agendamento_consulta_medica.service.PrescriptionItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/prescriptionItem")
public class PrescriptionItemResource {

    private final  PrescriptionItemService prescriptionItemService;

    public PrescriptionItemResource(PrescriptionItemService prescriptionItemService) {
        this.prescriptionItemService = prescriptionItemService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionItemDto> save(@RequestBody PrescriptionItemDto prescriptionItemDto){
        PrescriptionItemDto p = prescriptionItemService.save(prescriptionItemDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prescriptionItemDto.getPrescriptionId()).toUri();
        return ResponseEntity.created(uri).body(p);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionItemDto>> findAll(){
        List<PrescriptionItemDto> p = prescriptionItemService.findAll();
        return ResponseEntity.ok().body(p);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PrescriptionItemDto> findById(@PathVariable Long id) {
        PrescriptionItemDto p = prescriptionItemService.findById(id);
        return ResponseEntity.ok().body(p);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        prescriptionItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
