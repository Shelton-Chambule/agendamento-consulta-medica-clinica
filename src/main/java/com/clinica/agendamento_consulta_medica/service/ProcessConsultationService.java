package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.exception.ProcessConsultation;
import com.clinica.agendamento_consulta_medica.mapper.ConsultationMapper;
import com.clinica.agendamento_consulta_medica.repository.ConsultationRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProcessConsultationService extends ConsultationService {

    public ProcessConsultationService(ConsultationRepository consultationRepository, DoctorRepository doctorRepository, HistoryPatientService historyPatientService, ConsultationMapper consultationMapper, CurrentUserService currentUserService) {
        super(consultationRepository, doctorRepository, historyPatientService, consultationMapper, currentUserService);
    }

    @Transactional
    public ConsultationResponse processConsultationWaiting(Long id, Authentication authentication) {
        log.info("Confirming consultation with id: {}", id);
        Consultation consultation = getConsultation(id);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        if (consultation.getStatusConsultation() != StatusConsultation.WAITING) {
            log.warn("Consultation id {} is not in WAITING status and cannot be confirmed.", id);
            throw new ProcessConsultation("Only consultations in WAITING status can be confirmed.");
        }

        consultation.setStatusConsultation(StatusConsultation.CONFIRMED);
        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.updateStatus(saved);
        log.info("Consultation id {} confirmed successfully.", id);
        return consultationMapper.toResponse(saved);
    }

    @Transactional
    public ConsultationResponse processConsultationConfirmed(Long id, Authentication authentication) {
        log.info("Scheduling consultation with id: {}", id);
        Consultation consultation = getConsultation(id);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        if (consultation.getStatusConsultation() != StatusConsultation.CONFIRMED) {
            log.warn("Consultation id {} is not in CONFIRMED status and cannot be scheduled.", id);
            throw new ProcessConsultation("Only consultations in CONFIRMED status can be scheduled.");
        }

        consultation.setStatusConsultation(StatusConsultation.SCHEDULED);
        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.updateStatus(saved);
        log.info("Consultation id {} scheduled successfully.", id);
        return consultationMapper.toResponse(saved);
    }

    @Transactional
    public ConsultationResponse processConsultationScheduled(Long id, Authentication authentication) {
        log.info("Marking consultation as carried out, id: {}", id);
        Consultation consultation = getConsultation(id);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        if (consultation.getStatusConsultation() != StatusConsultation.SCHEDULED) {
            log.warn("Consultation id {} is not in SCHEDULED status and cannot be marked as carried out.", id);
            throw new ProcessConsultation("Only consultations in SCHEDULED status can be marked as carried out.");
        }

        consultation.setStatusConsultation(StatusConsultation.CARRIED_OUT);
        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.updateStatus(saved);
        log.info("Consultation id {} marked as carried out successfully.", id);
        return consultationMapper.toResponse(saved);
    }

    @Transactional
    public ConsultationResponse processConsultationCanceled(Long id, Authentication authentication) {
        log.info("Canceling consultation with id: {}", id);
        Consultation consultation = getConsultation(id);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        StatusConsultation currentStatus = consultation.getStatusConsultation();
        if (currentStatus == StatusConsultation.CARRIED_OUT  || currentStatus == StatusConsultation.CANCELED) {
            log.warn("Consultation id {} is already {} and cannot be canceled.", id, currentStatus);
            throw new ProcessConsultation("Consultations that are already carried out or canceled cannot be canceled.");
        }

        consultation.setStatusConsultation(StatusConsultation.CANCELED);
        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.updateStatus(saved);
        log.info("Consultation id {} canceled successfully.", id);
        return consultationMapper.toResponse(saved);
    }
}