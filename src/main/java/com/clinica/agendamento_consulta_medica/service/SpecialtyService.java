package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyRequest;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponse;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.Specialty;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.mapper.SpecialtyMapper;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.SpecialtyRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpecialtyService {

    private final SpecialtyRepository especialtyRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialtyMapper specialtyMapper;

    public SpecialtyService(SpecialtyRepository especialtyRepository, DoctorRepository doctorRepository,
                            SpecialtyMapper specialtyMapper) {
        this.especialtyRepository = especialtyRepository;
        this.doctorRepository = doctorRepository;
        this.specialtyMapper = specialtyMapper;
    }

    public SpecialtyResponse save(SpecialtyRequest specialtyRequestDTO) {
        log.info("Saving specialty for doctor id: {}", specialtyRequestDTO.getDoctorId());

        Doctor doctor = doctorRepository.findById(specialtyRequestDTO.getDoctorId()).orElseThrow(() -> {
            log.warn("Doctor not found with id: {}", specialtyRequestDTO.getDoctorId());
            return new ResourceNotFoundException(specialtyRequestDTO.getDoctorId());
        });

        Specialty specialty = new Specialty();

        specialty.setName(specialtyRequestDTO.getName());
        specialty.setDoctors(doctor);
        Specialty saved = especialtyRepository.save(specialty);
        log.info("Specialty saved successfully with id: {}", saved.getId());
        return specialtyMapper.toResponse(saved);
    }

    public Page<SpecialtyResponse> findAll(Pageable pageable) {
        log.debug("Fetching specialties page: {}", pageable);
        Page<Specialty> specialties = especialtyRepository.findAll(pageable);
        return specialtyMapper.toResponsePage(specialties);
    }

    public SpecialtyResponse findById(Long id) {
        log.debug("Fetching specialty with id: {}", id);
        Specialty specialty = especialtyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Specialty not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        return specialtyMapper.toResponse(specialty);
    }

    public void deleteById(Long id, Authentication authentication) {
        log.info("Deleting specialty with id: {}", id);

        especialtyRepository.findById(id).orElseThrow(() -> {
            log.warn("Specialty not found with id: {}", id);
            return new ResourceNotFoundException(id);
        });

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) {
            log.warn("User '{}' denied permission to delete specialties.", authentication.getName());
            throw new AccessDeniedException("You do not have permission to delete specialties.");
        }

        especialtyRepository.deleteById(id);
        log.info("Specialty id {} deleted successfully.", id);
    }

    public SpecialtyResponse update(Long id, SpecialtyRequest especialty, Authentication authentication) {
        log.info("Updating specialty with id: {}", id);

        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!admin) {
            log.warn("User '{}' denied permission to update specialties.", authentication.getName());
            throw new AccessDeniedException("You do not have permission to update specialties.");
        }

        try {
            Specialty especialty1 = especialtyRepository.getReferenceById(id);
            updateData(especialty1, especialty);
            especialtyRepository.save(especialty1);
            log.info("Specialty id {} updated successfully.", id);
            return specialtyMapper.toResponse(especialty1);
        } catch (EntityNotFoundException exception) {
            log.warn("Specialty not found with id: {}", id);
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