package com.clinica.agendamento_consulta_medica.service;

import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequest;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.exception.ProcessConsultation;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.ScheduleConflictException;
import com.clinica.agendamento_consulta_medica.repository.ConsultationRepository;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service

public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final HistoryPatientService historyPatientService;

    public ConsultationService(ConsultationRepository consultationRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, HistoryPatientService historyPatientService) {
        this.consultationRepository = consultationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.historyPatientService = historyPatientService;
    }

    @Transactional
    public ConsultationResponse save(ConsultationRequest request, Authentication authentication) {
        validateDuration(request.getDuration());
        Doctor doctor = doctorRepository.findById(request.getDoctor())
                .orElseThrow(() -> new ResourceNotFoundException(request.getDoctor()));
        Patient patient = patientRepository.findById(request.getPatient())
                .orElseThrow(() -> new ResourceNotFoundException(request.getPatient()));
        ensurePatientOwnerOrAdmin(patient, authentication);

        if (hasScheduleConflict(doctor, request.getDate(), request.getStartTime(), request.getDuration(), null)) {
            throw new ScheduleConflictException("The doctor already has an appointment scheduled for this time.");
        }

        Consultation consultation = new Consultation();
        consultation.setDate(request.getDate());
        consultation.setDuration(request.getDuration());
        consultation.setStartTime(request.getStartTime());
        consultation.setDoctor(doctor);
        consultation.setPatient(patient);
        consultation.setStatusConsultation(StatusConsultation.WAITING);

        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.save(saved);
        return new ConsultationResponse(saved);
    }

    @Transactional
    public ConsultationResponse processConsultation(Long id, Authentication authentication) {
        Consultation consultation = getConsultation(id);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        if (consultation.getStatusConsultation() != StatusConsultation.WAITING) {
            throw new ProcessConsultation("Only consultations in WAITING status can be processed.");
        }

        consultation.setStatusConsultation(StatusConsultation.CARRIED_OUT);
        Consultation saved = consultationRepository.save(consultation);
        historyPatientService.updateStatus(saved);
        return new ConsultationResponse(saved);
    }

    public List<ConsultationResponse> findAll() {
        return consultationRepository.findAll().stream().map(ConsultationResponse::new).toList();
    }

    public ConsultationResponse findById(Long id, Authentication authentication) {
        Consultation consultation = getConsultation(id);
        ensureParticipantOrAdmin(consultation, authentication);
        return new ConsultationResponse(consultation);
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        Consultation consultation = getConsultation(id);
        ensurePatientOwnerOrAdmin(consultation.getPatient(), authentication);
        ensureWaiting(consultation);

        try {
            consultationRepository.delete(consultation);
            consultationRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("The consultation cannot be deleted because it is referenced by other records.");
        }
    }

    @Transactional
    public ConsultationResponse update(Long id, ConsultationRequest request, Authentication authentication) {
        validateDuration(request.getDuration());
        Consultation consultation = getConsultation(id);
        ensurePatientOwnerOrAdmin(consultation.getPatient(), authentication);
        ensureWaiting(consultation);

        Doctor doctor = doctorRepository.findById(request.getDoctor())
                .orElseThrow(() -> new ResourceNotFoundException(request.getDoctor()));
        if (hasScheduleConflict(doctor, request.getDate(), request.getStartTime(), request.getDuration(), consultation.getId())) {
            throw new ScheduleConflictException("The doctor already has an appointment scheduled for this time.");
        }

        consultation.setDate(request.getDate());
        consultation.setStartTime(request.getStartTime());
        consultation.setDuration(request.getDuration());
        consultation.setDoctor(doctor);
        return new ConsultationResponse(consultationRepository.save(consultation));
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

    private Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
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
        throw new AccessDeniedException("You do not have permission to access this consultation.");
    }

    private void ensurePatientOwnerOrAdmin(Patient patient, Authentication authentication) {
        if (isAdmin(authentication) || patient.getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        throw new AccessDeniedException("You do not have permission to manage this patient's consultations.");
    }

    private void ensureDoctorOwnerOrAdmin(Consultation consultation, Authentication authentication) {
        if (isAdmin(authentication)
                || consultation.getDoctor().getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        throw new AccessDeniedException("You do not have permission to process this consultation.");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
