package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequest;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepository;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.service.exception.ValidateStatusConsultation;
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

    public PrescriptionService(PrescriptionRepository prescriptionRepsitory, ConsulationRepository consulationRepository) {
        this.prescriptionRepository = prescriptionRepsitory;
        this.consulationRepository = consulationRepository;

    }

    private Long  validateStatusConsultation(Long IdConsultation) {
        Consultation consultation = new Consultation();

        if(!consulationRepository.existsById(IdConsultation)){
            throw new ResourceNotFoundException(IdConsultation);
        }

        if (!(IdConsultation.equals(consultation.getId())) && consultation.getStatusConsultation().equals(StatusConsultation.CARRIED_OUT))
            throw new ValidateStatusConsultation("Erro creating of prescription, verify id  of consultation");

        return IdConsultation;
    }

    public PrescriptionResponse save(PrescriptionRequest prescriptionRequestDTO) {

        Consultation consultationId = consulationRepository.findById(prescriptionRequestDTO.getConsultationId()).orElseThrow(() ->
                new ResourceNotFoundException(prescriptionRequestDTO.getConsultationId()));

        Prescription prescription = new Prescription();

        prescription.setDosagem(prescriptionRequestDTO.getDosagem());
        prescription.setFrequency(prescriptionRequestDTO.getFrequency());
        prescription.setMedications(prescriptionRequestDTO.getMedications());
        prescription.setDate(LocalDate.now());
        prescription.setConsultation(consultationId);
        prescription.setObservations(prescriptionRequestDTO.getObservations());
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
        prescription.setDate(prescriptionDto.getDate());
    }
}
