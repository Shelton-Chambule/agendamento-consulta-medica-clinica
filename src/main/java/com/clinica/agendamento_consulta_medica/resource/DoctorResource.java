package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorDto;
import com.clinica.agendamento_consulta_medica.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping( "/doctor")
public class DoctorResource {


    private  final  DoctorService doctorService;

    public DoctorResource(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorDto> save(@RequestBody  DoctorDto doctor){
        DoctorDto doctors = doctorService.save(doctor);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(doctor.getId()).toUri();
        return ResponseEntity.created(uri).body(doctors);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> findAll(){
        List<DoctorDto> doctor = doctorService.findAll();
        return ResponseEntity.ok().body(doctor);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DoctorDto> findById(@PathVariable Long id){
        DoctorDto doctor = doctorService.findById(id);
        return ResponseEntity.ok().body(doctor);
    }

    @DeleteMapping(value = "/{id}")
    public  ResponseEntity<Void> deleteById(@PathVariable Long id){
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DoctorDto> update(@PathVariable Long id, @RequestBody DoctorDto doctor){
        DoctorDto doctor1 = doctorService.update(id,doctor);
        return ResponseEntity.ok().body(doctor1);
    }

}
