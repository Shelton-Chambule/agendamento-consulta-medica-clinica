package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientResponse;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.HistoryPatient;
import com.clinica.agendamento_consulta_medica.repository.HistoryPatientRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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

    public HistoryPatientResponse save(Consultation consultation) {
        HistoryPatient historyPatient = new HistoryPatient();

        historyPatient.setConsultation(consultation);
        historyPatient.setStatusConsultation(consultation.getStatusConsultation());
        historyPatientRepository.save(historyPatient);
        return new HistoryPatientResponse(historyPatient);
    }

    public List<HistoryPatientResponse> findAll() {
        List<HistoryPatient> historyPatient = historyPatientRepository.findAll();
        return historyPatient.stream().map(HistoryPatientResponse::new).collect(Collectors.toList());
    }

    public HistoryPatientResponse findById(Long id) {
        Optional<HistoryPatient> historyPatient = historyPatientRepository.findById(id);
        return new HistoryPatientResponse(historyPatient.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!historyPatientRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
            historyPatientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Transactional
    public void updateStatus(Consultation consultation) {
        HistoryPatient historyPatient = historyPatientRepository.findByConsultation_Id(consultation.getId());
            if(!historyPatientRepository.existsById(consultation.getId())) throw new ResourceNotFoundException(consultation.getId());

        historyPatient.setStatusConsultation(consultation.getStatusConsultation());

        historyPatientRepository.save(historyPatient);
    }
}
