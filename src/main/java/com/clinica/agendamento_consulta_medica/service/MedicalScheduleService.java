package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.mapper.MedicalScheduleMapper;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.MedicalScheduleRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalScheduleService {

    private final  MedicalScheduleRepository medicalScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalScheduleMapper medicalScheduleMapper;

    public MedicalSheduleResponse save(MedicalScheduleRequest medicalScheduleRequestDTO, Authentication authentication) {
        log.info("Saving medical schedule for doctor id: {}", medicalScheduleRequestDTO.getDoctorId());

        if (!medicalScheduleRequestDTO.getStarTime().isBefore(medicalScheduleRequestDTO.getEndTime())) {
            log.warn("Invalid schedule: start time is not before end time for doctor id {}", medicalScheduleRequestDTO.getDoctorId());
            throw new IllegalArgumentException("Schedule start time must be before end time.");
        }

        Doctor doctor = doctorRepository.findById(medicalScheduleRequestDTO.getDoctorId()).orElseThrow(() -> {
            log.warn("Doctor not found with id: {}", medicalScheduleRequestDTO.getDoctorId());
            return new ResourceNotFoundException(medicalScheduleRequestDTO.getDoctorId());
        });

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) {
            log.warn("User '{}' denied permission to create medical schedules.", authentication.getName());
            throw new AccessDeniedException("You do not have permission to create medical schedules.");
        }

        MedicalSchedule medicalSchedule = new MedicalSchedule();

        medicalSchedule.setStarTime(medicalScheduleRequestDTO.getStarTime());
        medicalSchedule.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedule.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        medicalSchedule.setDaysOfWeek(medicalScheduleRequestDTO.getDaysOfWeek());
        medicalSchedule.setDoctor(doctor);
        medicalScheduleRepository.save(medicalSchedule);
        log.info("Medical schedule saved successfully with id: {}", medicalSchedule.getId());
        return medicalScheduleMapper.toResponse(medicalSchedule);
    }

    public Page<MedicalSheduleResponse> findAll(Pageable pageable) {
        log.debug("Fetching medical schedules page: {}", pageable);
        Page<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findAll(pageable);
        return medicalScheduleMapper.toResponsePage(medicalSchedules);
    }

    public MedicalSheduleResponse findById(Long id) {
        log.debug("Fetching medical schedule with id: {}", id);
        MedicalSchedule medicalSchedule = medicalScheduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Medical schedule not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        return medicalScheduleMapper.toResponse(medicalSchedule);
    }

    public void deleteById(Long id , Authentication authentication) {
        log.info("Deleting medical schedule with id: {}", id);

        medicalScheduleRepository.findById(id).orElseThrow(() -> {
            log.warn("Medical schedule not found with id: {}", id);
            return new ResourceNotFoundException(id);
        });

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) {
            log.warn("User '{}' denied permission to delete medical schedules.", authentication.getName());
            throw new AccessDeniedException("You do not have permission to delete medical schedules.");
        }

        medicalScheduleRepository.deleteById(id);
        log.info("Medical schedule id {} deleted successfully.", id);
    }

    public MedicalSheduleResponse update(Long id, MedicalScheduleRequest medicalSchedule, Authentication authentication){
        log.info("Updating medical schedule with id: {}", id);

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) {
            log.warn("User '{}' denied permission to update medical schedules.", authentication.getName());
            throw new AccessDeniedException("You do not have permission to update medical schedules.");
        }

        MedicalSchedule medicalScheduleEntity = medicalScheduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Medical schedule not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
        updateDate(medicalScheduleEntity, medicalSchedule);
        medicalScheduleRepository.save(medicalScheduleEntity);
        log.info("Medical schedule id {} updated successfully.", id);
        return medicalScheduleMapper.toResponse(medicalScheduleEntity);
    }

    private void updateDate(MedicalSchedule medicalSchedules, MedicalScheduleRequest medicalScheduleRequestDTO) {
        if (!medicalScheduleRequestDTO.getStarTime().isBefore(medicalScheduleRequestDTO.getEndTime())) {
            throw new IllegalArgumentException("Schedule start time must be before end time.");
        }
        Doctor doctor = doctorRepository.findById(medicalScheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(medicalScheduleRequestDTO.getDoctorId()));
        medicalSchedules.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedules.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        medicalSchedules.setStarTime(medicalScheduleRequestDTO.getStarTime());
        medicalSchedules.setDaysOfWeek(medicalScheduleRequestDTO.getDaysOfWeek());
        medicalSchedules.setDoctor(doctor);
    }
}