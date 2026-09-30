package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.Prescription;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.ValidateStatusConsultation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsulationRepository consulationRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, ConsulationRepository consulationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.consulationRepository = consulationRepository;

    }

    public PrescriptionResponse save(PrescriptionRequest prescriptionRequestDTO, Authentication authentication) {

        Consultation consultation = consulationRepository.findById(prescriptionRequestDTO.getConsultationId()).orElseThrow(() -> new ResourceNotFoundException(prescriptionRequestDTO.getConsultationId()));

        if(!consultation.getStatusConsultation().equals(StatusConsultation.CARRIED_OUT)) throw new ValidateStatusConsultation("Erro creating of prescription, verify id  of consultation");

        boolean doctor = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_DOCTOR"));

        if(!doctor) throw new AccessDeniedException("Access denied");

        Prescription prescription = new Prescription();

        prescription.setDosage(prescriptionRequestDTO.getDosage());
        prescription.setFrequency(prescriptionRequestDTO.getFrequency());
        prescription.setMedications(prescriptionRequestDTO.getMedications());
        prescription.setObservations(prescriptionRequestDTO.getObservations());
        prescription.setConsultation(consultation);
        prescription.setDate(LocalDate.now());
        prescriptionRepository.save(prescription);
        return new PrescriptionResponse(prescription);

    }

    public List<PrescriptionResponse> findAll() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream().map(PrescriptionResponse::new).collect(Collectors.toList());
    }

    public PrescriptionResponse findById(Long id, Authentication authentication) {

        Optional<Prescription> prescription = prescriptionRepository.findById(id);

        boolean doctor = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_DOCTOR"));

        if(!doctor) throw new AccessDeniedException("Access denied");

        return new PrescriptionResponse(prescription.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id,Authentication authentication) {

        prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        boolean doctor = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_DOCTOR"));
        boolean admin = authentication.getAuthorities().stream().anyMatch((any -> any.getAuthority().equals("ROLE_ADMIN")));

        if(!doctor && !admin) throw new AccessDeniedException("Access denied");

        prescriptionRepository.deleteById(id);
    }

    public PrescriptionResponse update (Long id, PrescriptionRequest prescriptionDto , Authentication authentication) {

        boolean doctor = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_DOCTOR"));
        boolean admin = authentication.getAuthorities().stream().anyMatch((any -> any.getAuthority().equals("ROLE_ADMIN")));

        if(!doctor && !admin) throw new AccessDeniedException("Access denied");

        try {
            Prescription prescription = prescriptionRepository.getReferenceById(id);
            updateData(prescription, prescriptionDto);
            prescriptionRepository.save(prescription);
            return new PrescriptionResponse(prescription);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Prescription prescription, PrescriptionRequest prescriptionDto) {
        
        Consultation consultation = consulationRepository.findById(prescriptionDto.getConsultationId()).orElseThrow(() -> new ResourceNotFoundException(prescriptionDto.getConsultationId()));

        prescription.setObservations(prescriptionDto.getObservations());
        prescription.setDosage(prescription.getDosage());
        prescription.setMedications(prescriptionDto.getMedications());
        prescription.setDate(prescriptionDto.getDate());
        prescription.setConsultation(consultation);
    }
}
