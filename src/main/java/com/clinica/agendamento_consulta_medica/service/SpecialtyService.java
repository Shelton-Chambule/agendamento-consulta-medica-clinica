package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequest;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponse;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.Specialty;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.SpecialtyRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
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

    public SpecialtyResponse save(SpecialtyRequest specialtyRequestDTO) {

        Doctor doctor = doctorRepository.findById(specialtyRequestDTO.getDoctorId()).orElseThrow(() ->
                new ResourceNotFoundException(specialtyRequestDTO.getDoctorId()));

        Specialty specialty = new Specialty();

        specialty.setName(specialtyRequestDTO.getName());
        specialty.setDoctors(doctor);
        especialtyRepository.save(specialty);
        return new SpecialtyResponse(specialty);
    }

    public List<SpecialtyResponse> findAll() {
        List<Specialty> specialty = especialtyRepository.findAll();
        return specialty.stream().map(SpecialtyResponse::new).collect(Collectors.toList());
    }

    public SpecialtyResponse findById(Long id, Authentication authentication) {

        Optional<Specialty> especialty = especialtyRepository.findById(id);

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw  new AccessDeniedException(" Access denied,  you don,t  authorization");

        return new SpecialtyResponse(especialty.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id, Authentication authentication) {

        especialtyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw new AccessDeniedException("Access denied!");

        especialtyRepository.deleteById(id);
    }

    public SpecialtyResponse update(Long id, SpecialtyRequest especialty) {

        try {
            Specialty especialty1 = especialtyRepository.getReferenceById(id);
            updateData(especialty1, especialty);
            especialtyRepository.save(especialty1);
            return new SpecialtyResponse(especialty1);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Specialty especialty1, SpecialtyRequest specialtyRequestDTO) {
        Doctor doctor = doctorRepository.findById(specialtyRequestDTO.getDoctorId()).orElseThrow(() ->
                new ResourceNotFoundException(specialtyRequestDTO.getDoctorId()));

        especialty1.setName(specialtyRequestDTO.getName());
        especialty1.setDoctors(doctor);
    }
}
