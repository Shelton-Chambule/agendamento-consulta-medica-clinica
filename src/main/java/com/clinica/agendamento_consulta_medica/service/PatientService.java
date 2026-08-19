package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientDto;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private  final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDto save(PatientDto patient) {
        Patient patient1 = new Patient();
        patient1.setPatientId(patient.getPatientId());
        patient1.setName(patient.getName());
        patient1.setEmail(patient.getEmail());
        patient1.setPhone(patient.getPhone());
        patientRepository.save(patient1);
        return new PatientDto(patient1);
    }

    public List<PatientDto> findAll() {
        List<Patient> patientList = patientRepository.findAll();
        return patientList.stream().map(PatientDto::new).collect(Collectors.toList());
    }

    public PatientDto findById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return new PatientDto(patient.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public PatientDto update(Long id, PatientDto patient) {
        try{
            Patient patient1 = patientRepository.getReferenceById(id);
            updateData(patient1, patient);
            patientRepository.save(patient1);
            return new PatientDto(patient1);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Patient patient1, PatientDto patient) {
        patient1.setName(patient.getName());
        patient1.setPhone(patient.getPhone());
        patient1.setEmail(patient.getEmail());
    }

    public void deleteById(Long id) {
        if(!patientRepository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }

        try{
            patientRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException(e.getMessage());
        }
    }
}
