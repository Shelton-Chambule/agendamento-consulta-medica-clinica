package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import com.clinica.agendamento_consulta_medica.repository.PatientRepository;
import com.clinica.agendamento_consulta_medica.service.exception.DataBaseException;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import com.clinica.agendamento_consulta_medica.service.exception.UniqueEmailException;
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

    public String   validateEmail(String email){
        if(patientRepository.existsByEmail(email)){
            throw new UniqueEmailException("This email already register!");
        }
        return email;
    }

    public PatientResponseDTO save(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(validateEmail(patientRequestDTO.getEmail()));
        patient.setPhone(patientRequestDTO.getPhone());
        patientRepository.save(patient);
        return new PatientResponseDTO(patient);
    }

    public List<PatientResponseDTO> findAll() {
        List<Patient> patientList = patientRepository.findAll();
        return patientList.stream().map(PatientResponseDTO::new).collect(Collectors.toList());
    }

    public PatientResponseDTO findById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return new PatientResponseDTO(patient.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public PatientResponseDTO update(Long id, PatientRequestDTO patient) {
        try{
            Patient patient1 = patientRepository.getReferenceById(id);
            updateData(patient1, patient);
            patientRepository.save(patient1);
            return new PatientResponseDTO(patient1);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Patient patient1, PatientRequestDTO patient) {
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
