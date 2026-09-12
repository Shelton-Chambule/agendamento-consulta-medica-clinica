package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import com.clinica.agendamento_consulta_medica.repository.ConsulationRepository;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepsitory;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.service.exception.ValidateStatusConsultation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepsitory prescriptionRepsitory;
    private final ConsulationRepository consulationRepository;

    public PrescriptionService(PrescriptionRepsitory prescriptionRepsitory, ConsulationRepository consulationRepository) {
        this.prescriptionRepsitory = prescriptionRepsitory;
        this.consulationRepository = consulationRepository;

    }

    private Long  validateStatusConsultation(Long IdConsultation) {
        Consultation consultation = new Consultation();

        if(!consulationRepository.existsById(IdConsultation)){
            throw new ResourceNotFoundException(IdConsultation);
        }

        if (!(IdConsultation.equals(consultation.getId())) && consultation.getStatusConsultation().equals(StatusConsultation.CARRIED_OUT)) {
            throw new ValidateStatusConsultation("Erro creating of prescription, verify id  of consultation");
        }
        return IdConsultation;
    }

    public PrescriptionResponseDTO save(PrescriptionRequestDTO prescriptionRequestDTO) {

        Consultation consultationId = consulationRepository.findById(prescriptionRequestDTO.getConsultationId()).orElseThrow(() ->
                new ResourceNotFoundException(prescriptionRequestDTO.getConsultationId()));

        Prescription prescription = new Prescription();

        prescription.setDosagem(prescriptionRequestDTO.getDosagem());
        prescription.setFrequency(prescriptionRequestDTO.getFrequency());
        prescription.setMedications(prescriptionRequestDTO.getMedications());
        prescription.setDate(LocalDate.now());
        prescription.setConsultation(consultationId);
        prescription.setObservations(prescriptionRequestDTO.getObservations());
        prescriptionRepsitory.save(prescription);
        return new PrescriptionResponseDTO(prescription);
    }

    public List<PrescriptionResponseDTO> findAll() {
        List<Prescription> prescriptions = prescriptionRepsitory.findAll();
        return prescriptions.stream().map(PrescriptionResponseDTO::new).collect(Collectors.toList());
    }

    public PrescriptionResponseDTO findById(Long id) {
        Optional<Prescription> prescription = prescriptionRepsitory.findById(id);
        return new PrescriptionResponseDTO(prescription.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!prescriptionRepsitory.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        prescriptionRepsitory.deleteById(id);
    }

    public PrescriptionResponseDTO update(PrescriptionResponseDTO prescriptionDto, Long id) {
        try {
            Prescription prescription = prescriptionRepsitory.getReferenceById(id);
            updateData(prescription, prescriptionDto);
            prescriptionRepsitory.save(prescription);
            return new PrescriptionResponseDTO(prescription);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Prescription prescription, PrescriptionResponseDTO prescriptionDto) {
        prescription.setObservations(prescriptionDto.getObservations());
        prescription.setDate(prescriptionDto.getDate());
    }
}
