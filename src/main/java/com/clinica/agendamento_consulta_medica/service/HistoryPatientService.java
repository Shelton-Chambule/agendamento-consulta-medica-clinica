package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientDto;
import com.clinica.agendamento_consulta_medica.entities.HistoryPatient;
import com.clinica.agendamento_consulta_medica.repository.HistoryPatientRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryPatientService {


    private final HistoryPatientRepository historyPatientRepository;

    public HistoryPatientService(HistoryPatientRepository historyPatientRepository) {
        this.historyPatientRepository = historyPatientRepository;
    }

    public HistoryPatientDto save(HistoryPatientDto historyPatientDto){
        HistoryPatient historyPatient = new HistoryPatient();
        historyPatient.setId(historyPatientDto.getId());
        historyPatient.setConsultations(historyPatient.getConsultations());
        historyPatient.setStatusConsultation(historyPatientDto.getStatusConsultation());
        historyPatientRepository.save(historyPatient);
        return new HistoryPatientDto(historyPatient);
    }
    public List<HistoryPatientDto> findAll(){
        List<HistoryPatient> historyPatient = historyPatientRepository.findAll();
        return  historyPatient.stream().map(HistoryPatientDto::new).collect(Collectors.toList());
    }

    public  HistoryPatientDto findById(Long id){
        Optional<HistoryPatient>  historyPatient = historyPatientRepository.findById(id);
        return  new HistoryPatientDto(historyPatient.orElseThrow(() -> new ResourceNotFoundException(id)));
    }
    public void deleteById(Long id){
       if(!historyPatientRepository.existsById(id)){
           throw new ResourceNotFoundException(id);
       }
       try{
           historyPatientRepository.deleteById(id);
       }catch (DataIntegrityViolationException e){
           throw new DataBaseException(e.getMessage());
       }
    }
}
