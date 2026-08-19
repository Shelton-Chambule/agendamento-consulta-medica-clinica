package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionDto;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import com.clinica.agendamento_consulta_medica.repository.PrescriptionRepsitory;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepsitory prescriptionRepsitory;

    public PrescriptionService(PrescriptionRepsitory prescriptionRepsitory) {
        this.prescriptionRepsitory = prescriptionRepsitory;
    }

    public PrescriptionDto save(PrescriptionDto prescriptionDto) {
        Prescription prescription = new Prescription();
        prescription.setValidity(prescriptionDto.getValidity());
        prescription.setDate(prescriptionDto.getDate());
        prescription.setObservations(prescriptionDto.getObservations());
        prescriptionRepsitory.save(prescription);
        return new PrescriptionDto(prescription);
    }

    public List<PrescriptionDto> findAll() {
        List<Prescription> prescriptions =prescriptionRepsitory.findAll();
        return prescriptions.stream().map(PrescriptionDto::new).collect(Collectors.toList());
    }

    public PrescriptionDto findById(Long id) {
        Optional<Prescription> prescription = prescriptionRepsitory.findById(id);
        return new PrescriptionDto(prescription.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!prescriptionRepsitory.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        prescriptionRepsitory.deleteById(id);
    }

    public PrescriptionDto update(PrescriptionDto prescriptionDto, Long id) {
        try {
            Prescription prescription = prescriptionRepsitory.getReferenceById(id);
            updateData(prescription,prescriptionDto);
            prescriptionRepsitory.save(prescription);
            return new PrescriptionDto(prescription);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Prescription prescription, PrescriptionDto prescriptionDto) {
        prescription.setObservations(prescriptionDto.getObservations());
        prescription.setDate(prescriptionDto.getDate());
        prescription.setValidity(prescriptionDto.getValidity());
    }
}
