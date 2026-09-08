package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequestDTO;
import com.clinica.agendamento_consulta_medica.service.SpecialtyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/specialty")
public class SpecialtyResource {

    private  final  SpecialtyService especialtyService;

    public SpecialtyResource(SpecialtyService especialtyService) {
        this.especialtyService = especialtyService;
    }

    @PostMapping
    public ResponseEntity<SpecialtyRequestDTO> save(@RequestBody SpecialtyRequestDTO specialty){
        SpecialtyRequestDTO specialtys = especialtyService.save(specialty);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(specialty.getId()).toUri();
        return ResponseEntity.created(uri).body(specialtys);
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyRequestDTO>> findAll(){
        List<SpecialtyRequestDTO> specialty = especialtyService.findAll();
        return ResponseEntity.ok().body(specialty);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SpecialtyRequestDTO> findById(@PathVariable Long id){
        SpecialtyRequestDTO specialty = especialtyService.findById(id);
        return ResponseEntity.ok().body(specialty);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        especialtyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/{id}")
    public  ResponseEntity<SpecialtyRequestDTO> update(@PathVariable Long id, @RequestBody SpecialtyRequestDTO specialty){
        SpecialtyRequestDTO specialty1 = especialtyService.update(id,specialty);
        return ResponseEntity.ok().body(specialty1);
    }

}
