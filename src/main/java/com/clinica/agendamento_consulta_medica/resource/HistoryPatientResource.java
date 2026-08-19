package com.clinica.agendamento_consulta_medica.resource;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientDto;
import com.clinica.agendamento_consulta_medica.service.HistoryPatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping( "/historyConsultation")
public class HistoryPatientResource {


    private final HistoryPatientService historyPatientService;

    public HistoryPatientResource(HistoryPatientService historyPatientService) {
        this.historyPatientService = historyPatientService;
    }

    @PostMapping
    public ResponseEntity<HistoryPatientDto> save(@RequestBody HistoryPatientDto historyPatientDto){
        HistoryPatientDto historyPatientDto1 = historyPatientService.save(historyPatientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(historyPatientDto.getIdPatient()).toUri();
        return ResponseEntity.created(uri).body(historyPatientDto1);
    }
    @GetMapping
    public ResponseEntity<List<HistoryPatientDto>> findAll() {
        List<HistoryPatientDto> historyPatient = historyPatientService.findAll();
        return ResponseEntity.ok().body(historyPatient);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HistoryPatientDto> findById(@PathVariable Long id) {
        HistoryPatientDto historyPatient = historyPatientService.findById(id);
        return ResponseEntity.ok().body(historyPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable  Long id){
        historyPatientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
