package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequest;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.entity.Account;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ProcessConsultation;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.ScheduleConflictException;
import com.clinica.agendamento_consulta_medica.mapper.ConsultationMapper;
import com.clinica.agendamento_consulta_medica.repository.ConsultationRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationService {

    protected final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;
    protected final HistoryPatientService historyPatientService;
    protected final ConsultationMapper consultationMapper;
    private final CurrentUserService currentUserService;

    @Transactional
    public ConsultationResponse save(ConsultationRequest request) {

        Account account = currentUserService.getCurrentUser();
        Patient patient = account.getPatient();

        if (patient == null) {
            log.warn("User '{}' tried to schedule a consultation but has no associated patient profile.", account.getLogin());
            throw new AccessDeniedException("Only patients can schedule consultations.");
        }

        log.info("Saving consultation for doctor id {} and patient id {}", request.getDoctor(), patient.getPatientId());
        validateDuration(request.getDuration());

        Doctor doctor = doctorRepository.findById(request.getDoctor())
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", request.getDoctor());
                    return new ResourceNotFoundException(request.getDoctor());
                });

        if (hasScheduleConflict(doctor, request.getDate(), request.getStartTime(), request.getDuration(), null)) {
            log.warn("Schedule conflict detected for doctor id {} on {} at {}", doctor.getDoctorId(), request.getDate(), request.getStartTime());
            throw new ScheduleConflictException("The doctor already has an appointment scheduled for this time.");
        }

        Consultation consultation = new Consultation();
        consultation.setDate(request.getDate());
        consultation.setDuration(request.getDuration());
        consultation.setStartTime(request.getStartTime());
        consultation.setConsultationDescription(request.getConsultationDescription());
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setStatusConsultation(StatusConsultation.WAITING);

        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.save(saved);
        log.info("Consultation saved successfully with id: {}", saved.getId());
        return consultationMapper.toResponse(saved);
    }

    public Page<ConsultationResponse> findAll(Pageable pageable) {
        log.debug("Fetching consultations page: {}", pageable);
        Page<Consultation> consultationPage = consultationRepository.findAll(pageable);
        return consultationMapper.toResponsePage(consultationPage);
    }

    public ConsultationResponse findById(Long id, Authentication authentication) {
        log.debug("Fetching consultation with id: {}", id);
        Consultation consultation = getConsultation(id);
        ensureParticipantOrAdmin(consultation, authentication);
        return consultationMapper.toResponse(consultation);
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        log.info("Deleting consultation with id: {}", id);
        Consultation consultation = getConsultation(id);
        ensurePatientOwnerOrAdmin(consultation.getPatient(), authentication);
        ensureWaiting(consultation);

        try {
            consultationRepository.delete(consultation);
            consultationRepository.flush();
            log.info("Consultation id {} deleted successfully.", id);
        } catch (DataIntegrityViolationException exception) {
            log.error("Failed to delete consultation id {} due to a data integrity violation.", id, exception);
            throw new DataBaseException("The consultation cannot be deleted because it is referenced by other records.");
        }
    }

    @Transactional
    public ConsultationResponse update(Long id, ConsultationRequest request, Authentication authentication) {
        log.info("Updating consultation with id: {}", id);
        validateDuration(request.getDuration());
        Consultation consultation = getConsultation(id);
        ensurePatientOwnerOrAdmin(consultation.getPatient(), authentication);
        ensureWaiting(consultation);

        Doctor doctor = doctorRepository.findById(request.getDoctor())
                .orElseThrow(() -> {
                    log.warn("Doctor not found with id: {}", request.getDoctor());
                    return new ResourceNotFoundException(request.getDoctor());
                });
        if (hasScheduleConflict(doctor, request.getDate(), request.getStartTime(), request.getDuration(), consultation.getId())) {
            log.warn("Schedule conflict detected while updating consultation id {}", id);
            throw new ScheduleConflictException("The doctor already has an appointment scheduled for this time.");
        }

        consultation.setDate(request.getDate());
        consultation.setStartTime(request.getStartTime());
        consultation.setDuration(request.getDuration());
        consultation.setDoctor(doctor);
        Consultation saved = consultationRepository.save(consultation);
        log.info("Consultation id {} updated successfully.", id);
        return consultationMapper.toResponse(saved);
    }


    private boolean hasScheduleConflict(Doctor doctor, LocalDate date, LocalTime startTime,
                                        Duration duration, Long excludedConsultationId) {
        LocalTime endTime = startTime.plus(duration);
        return doctor.getConsultations().stream()
                .filter(existing -> !existing.getId().equals(excludedConsultationId))
                .filter(existing -> existing.getDate().equals(date))
                .anyMatch(existing -> startTime.isBefore(existing.getStartTime().plus(existing.getDuration()))
                        && existing.getStartTime().isBefore(endTime));
    }

    protected Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElseThrow(() -> {
            log.warn("Consultation not found with id: {}", id);
            return new ResourceNotFoundException(id);
        });
    }

    private void validateDuration(Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("Consultation duration must be greater than zero.");
        }
    }

    private void ensureWaiting(Consultation consultation) {
        if (consultation.getStatusConsultation() != StatusConsultation.WAITING) {
            throw new ProcessConsultation("Only consultations in WAITING status can be changed or cancelled.");
        }
    }

    private void ensureParticipantOrAdmin(Consultation consultation, Authentication authentication) {
        if (isAdmin(authentication)
                || consultation.getPatient().getAccount().getLogin().equals(authentication.getName())
                || consultation.getDoctor().getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        log.warn("User '{}' denied access to consultation id {}", authentication.getName(), consultation.getId());
        throw new AccessDeniedException("You do not have permission to access this consultation.");
    }

    private void ensurePatientOwnerOrAdmin(Patient patient, Authentication authentication) {
        if (isAdmin(authentication) || patient.getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        log.warn("User '{}' denied management of patient id {}'s consultations", authentication.getName(), patient.getPatientId());
        throw new AccessDeniedException("You do not have permission to manage this patient's consultations.");
    }

    protected void ensureDoctorOwnerOrAdmin(Consultation consultation, Authentication authentication) {
        if (isAdmin(authentication) || consultation.getDoctor().getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        log.warn("User '{}' denied processing of consultation id {}", authentication.getName(), consultation.getId());
        throw new AccessDeniedException("You do not have permission to process this consultation.");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}