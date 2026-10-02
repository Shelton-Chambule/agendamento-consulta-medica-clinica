package com.clinica.agendamento_consulta_medica.service;

import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.Prescription;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.ValidateStatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsulationRepository consultationRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, ConsulationRepository consultationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.consultationRepository = consultationRepository;
    }

    @Transactional
    public PrescriptionResponse save(PrescriptionRequest request, Authentication authentication) {
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
        return new PrescriptionResponse(prescriptionRepository.save(prescription));
    }

    public List<PrescriptionResponse> findAll() {
        return prescriptionRepository.findAll().stream().map(PrescriptionResponse::new).toList();
    }

    public PrescriptionResponse findById(Long id, Authentication authentication) {
        Prescription prescription = getPrescription(id);
        ensureParticipantOrAdmin(prescription, authentication);
        return new PrescriptionResponse(prescription);
    }

    @Transactional
    public void deleteById(Long id, Authentication authentication) {
        Prescription prescription = getPrescription(id);
        ensureDoctorOwnerOrAdmin(prescription.getConsultation(), authentication);
        prescriptionRepository.delete(prescription);
    }

    @Transactional
    public PrescriptionResponse update(Long id, PrescriptionRequest request, Authentication authentication) {
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
        return new PrescriptionResponse(prescriptionRepository.save(prescription));
    }

    private Prescription getPrescription(Long id) {
        return prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private Consultation getConsultation(Long id) {
        return consultationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private void ensureCarriedOut(Consultation consultation) {
        if (consultation.getStatusConsultation() != StatusConsultation.CARRIED_OUT) {
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
        throw new AccessDeniedException("You do not have permission to access this prescription.");
    }

    private void ensureDoctorOwnerOrAdmin(Consultation consultation, Authentication authentication) {
        if (isAdmin(authentication)
                || consultation.getDoctor().getAccount().getLogin().equals(authentication.getName())) {
            return;
        }
        throw new AccessDeniedException("You do not have permission to manage this prescription.");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
