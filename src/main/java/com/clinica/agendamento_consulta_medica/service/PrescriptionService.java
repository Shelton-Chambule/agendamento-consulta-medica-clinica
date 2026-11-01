package com.clinica.agendamento_consulta_medica.service;

import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.Prescription;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.ValidateStatusConsultation;
import com.clinica.agendamento_consulta_medica.mapper.PrescriptionMapper;
import com.clinica.agendamento_consulta_medica.repository.ConsultationRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, ConsultationRepository consultationRepository,
                               PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.consultationRepository = consultationRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    @Transactional
    public PrescriptionResponse save(PrescriptionRequest request, Authentication authentication) {
        log.info("Saving prescription for consultation id: {}", request.getConsultationId());
        Consultation consultation = getConsultation(request.getConsultationId());
        ensureCarriedOut(consultation);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        Prescription prescription = new Prescription();
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setMedications(request.getMedications());
        prescription.setObservations(request.getObservations());
        prescription.setConsultation(consultation);
        prescription.setDate(LocalDate.now());
        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Prescription saved successfully with id: {}", saved.getId());
        return prescriptionMapper.toResponse(saved);
    }

    public Page<PrescriptionResponse> findAll(Pageable pageable) {
        log.debug("Fetching prescriptions page: {}", pageable);
        Page<Prescription> prescriptions = prescriptionRepository.findAll(pageable);
        return prescriptionMapper.toResponsePage(prescriptions);
    }

    public PrescriptionResponse findById(Long id, Authentication authentication) {
        log.debug("Fetching prescription with id: {}", id);
        Prescription prescription = getPrescription(id);
        ensureParticipantOrAdmin(prescription, authentication);
        return prescriptionMapper.toResponse(prescription);
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        log.info("Deleting prescription with id: {}", id);
        Prescription prescription = getPrescription(id);
        ensureDoctorOwnerOrAdmin(prescription.getConsultation(), authentication);
        prescriptionRepository.delete(prescription);
        log.info("Prescription id {} deleted successfully.", id);
    }

    @Transactional
    public PrescriptionResponse update(Long id, PrescriptionRequest request, Authentication authentication) {
        log.info("Updating prescription with id: {}", id);
        Prescription prescription = getPrescription(id);
        ensureDoctorOwnerOrAdmin(prescription.getConsultation(), authentication);

        Consultation consultation = getConsultation(request.getConsultationId());
        ensureCarriedOut(consultation);
        ensureDoctorOwnerOrAdmin(consultation, authentication);

        prescription.setObservations(request.getObservations());
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setMedications(request.getMedications());
        prescription.setConsultation(consultation);
        Prescription saved = prescriptionRepository.save(prescription);
        log.info("Prescription id {} updated successfully.", id);
        return prescriptionMapper.toResponse(saved);
    }

    private Prescription getPrescription(Long id) {
        return prescriptionRepository.findById(id).orElseThrow(() -> {
            log.warn("Prescription not found with id: {}", id);
            return new ResourceNotFoundException(id);
        });
    }

    private Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElseThrow(() -> {
            log.warn("Consultation not found with id: {}", id);
            return new ResourceNotFoundException(id);
        });
    }

    private void ensureCarriedOut(Consultation consultation) {
        if (consultation.getStatusConsultation() != StatusConsultation.CARRIED_OUT) {
            log.warn("Consultation id {} is not CARRIED_OUT; prescription cannot be created.", consultation.getId());
            throw new ValidateStatusConsultation(
                    "A prescription can only be created for a completed consultation.");
        }
    }

    private void ensureParticipantOrAdmin(Prescription prescription, Authentication authentication) {
        Consultation consultation = prescription.getConsultation();
        if (isAdmin(authentication)
                || consultation.getDoctor().getAccount().getLogin().equals(authentication.getName())
                || consultation.getPatient().getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        log.warn("User '{}' denied access to prescription id {}", authentication.getName(), prescription.getId());
        throw new AccessDeniedException("You do not have permission to access this prescription.");
    }

    private void ensureDoctorOwnerOrAdmin(Consultation consultation, Authentication authentication) {
        if (isAdmin(authentication)
                || consultation.getDoctor().getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        log.warn("User '{}' denied management of prescription for consultation id {}", authentication.getName(), consultation.getId());
        throw new AccessDeniedException("You do not have permission to manage this prescription.");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}