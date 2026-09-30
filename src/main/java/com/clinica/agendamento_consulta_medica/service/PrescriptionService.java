package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import com.clinica.agendamento_consulta_medica.entity.Prescription;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.exception.ValidateStatusConsultation;
import jakarta.persistence.EntityNotFoundException;
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

    public PrescriptionResponse save(PrescriptionRequest prescriptionRequestDTO) {

        Consultation consultation = consulationRepository.findById(prescriptionRequestDTO.getConsultationId()).orElseThrow(() -> new ResourceNotFoundException(prescriptionRequestDTO.getConsultationId()));

        if(!consultation.getStatusConsultation().equals(StatusConsultation.CARRIED_OUT)) throw new ValidateStatusConsultation("Erro creating of prescription, verify id  of consultation");

        Prescription prescription = new Prescription();

        prescription.setDosagem(prescriptionRequestDTO.getDosagem());
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

    public PrescriptionResponse findById(Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        return new PrescriptionResponse(prescription.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        prescriptionRepository.deleteById(id);
    }

    public PrescriptionResponse update (Long id, PrescriptionRequest prescriptionDto) {
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
        prescription.setObservations(prescriptionDto.getObservations());
        prescription.setDosagem(prescription.getDosagem());
        prescription.setMedications(prescriptionDto.getMedications());
        prescription.setDate(prescriptionDto.getDate());
    }
}
