package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.HistoryPatient;
import com.clinica.agendamento_consulta_medica.mapper.HistoryPatientMapper;
import com.clinica.agendamento_consulta_medica.repository.HistoryPatientRepository;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class HistoryPatientService {

    private final HistoryPatientRepository historyPatientRepository;
    private final HistoryPatientMapper historyPatientMapper;

    public HistoryPatientService(HistoryPatientRepository historyPatientRepository, HistoryPatientMapper historyPatientMapper) {
        this.historyPatientRepository = historyPatientRepository;
        this.historyPatientMapper = historyPatientMapper;
    }

    public HistoryPatientResponse save(Consultation consultation) {
        log.info("Saving history for consultation id: {}", consultation.getId());
        HistoryPatient historyPatient = new HistoryPatient();

        historyPatient.setConsultation(consultation);
        historyPatient.setStatusConsultation(consultation.getStatusConsultation());
        HistoryPatient saved = historyPatientRepository.save(historyPatient);
        log.info("History saved successfully with id: {}", saved.getId());
        return historyPatientMapper.toResponse(saved);
    }

    public Page<HistoryPatientResponse> findAll(Pageable pageable) {
        log.debug("Fetching consultation history page: {}", pageable);
        Page<HistoryPatient> historyPatientPage = historyPatientRepository.findAll(pageable);
        return historyPatientMapper.toResponsePage(historyPatientPage);
    }

    public HistoryPatientResponse findById(Long id) {
        log.debug("Fetching history with id: {}", id);
        Optional<HistoryPatient> historyPatient = historyPatientRepository.findById(id);
        return historyPatientMapper.toResponse(historyPatient.orElseThrow(() -> {
            log.warn("History not found with id: {}", id);
            return new ResourceNotFoundException(id);
        }));
    }

    public void deleteById(Long id) {
        log.info("Deleting history with id: {}", id);
        if (!historyPatientRepository.existsById(id)) {
            log.warn("History not found with id: {}", id);
            throw new ResourceNotFoundException(id);
        }
        try {
            historyPatientRepository.deleteById(id);
            log.info("History id {} deleted successfully.", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete history id {} due to a data integrity violation.", id, e);
            throw new DataBaseException("The consultation history cannot be deleted because it is referenced by other records.");
        }
    }

    @Transactional
    public void updateStatus(Consultation consultation) {
        log.info("Updating history status for consultation id: {}", consultation.getId());
        HistoryPatient historyPatient = historyPatientRepository.findByConsultation_Id(consultation.getId());
        if (historyPatient == null) {
            log.warn("History not found for consultation id: {}", consultation.getId());
            throw new ResourceNotFoundException(consultation.getId());
        }

        historyPatient.setStatusConsultation(consultation.getStatusConsultation());

        historyPatientRepository.save(historyPatient);
        log.info("History status updated successfully for consultation id: {}", consultation.getId());
    }
}