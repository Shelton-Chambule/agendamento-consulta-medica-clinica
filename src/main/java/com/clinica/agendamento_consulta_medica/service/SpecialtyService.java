package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.SpecialtyRepository;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    private final SpecialtyRepository especialtyRepository;
    private final DoctorRepository doctorRepository;

    public SpecialtyService(SpecialtyRepository especialtyRepository, DoctorRepository doctorRepository) {
        this.especialtyRepository = especialtyRepository;
        this.doctorRepository = doctorRepository;
    }

    public SpecialtyResponseDTO save(SpecialtyRequestDTO specialtyRequestDTO) {

        Doctor doctor = doctorRepository.findById(specialtyRequestDTO.getDoctorId()).orElseThrow(() ->
                new ResourceNotFoundException(specialtyRequestDTO.getDoctorId()));

        Specialty specialty = new Specialty();

        specialty.setName(specialtyRequestDTO.getName());
        specialty.setPrice(specialtyRequestDTO.getPrice());
        specialty.setDoctors(doctor);
        especialtyRepository.save(specialty);
        return new SpecialtyResponseDTO(specialty);
    }

    public List<SpecialtyResponseDTO> findAll() {
        List<Specialty> specialty = especialtyRepository.findAll();
        return specialty.stream().map(SpecialtyResponseDTO::new).collect(Collectors.toList());
    }

    public SpecialtyResponseDTO findById(Long id) {
        Optional<Specialty> especialty = especialtyRepository.findById(id);
        return new SpecialtyResponseDTO(especialty.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {
        if (!especialtyRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        especialtyRepository.deleteById(id);
    }

    public SpecialtyResponseDTO update(Long id, SpecialtyRequestDTO especialty) {

        try {
            Specialty especialty1 = especialtyRepository.getReferenceById(id);
            updateData(especialty1, especialty);
            especialtyRepository.save(especialty1);
            return new SpecialtyResponseDTO(especialty1);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Specialty especialty1, SpecialtyRequestDTO specialtyRequestDTO) {
        Doctor doctor = doctorRepository.findById(specialtyRequestDTO.getDoctorId()).orElseThrow(() ->
                new ResourceNotFoundException(specialtyRequestDTO.getDoctorId()));

        especialty1.setName(specialtyRequestDTO.getName());
        especialty1.setDoctors(doctor);
        especialty1.setPrice(specialtyRequestDTO.getPrice());
    }
}
