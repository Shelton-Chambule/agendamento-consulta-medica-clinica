package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.MedicalScheduleRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalScheduleService {

    private final  MedicalScheduleRepository medicalScheduleRepository;
    private final DoctorRepository doctorRepository;

    public MedicalSheduleResponse save(MedicalScheduleRequest medicalScheduleRequestDTO, Authentication authentication) {

        if (!medicalScheduleRequestDTO.getStarTime().isBefore(medicalScheduleRequestDTO.getEndTime())) {
            throw new IllegalArgumentException("Schedule start time must be before end time.");
        }

        Doctor doctor = doctorRepository.findById(medicalScheduleRequestDTO.getDoctorId()).orElseThrow(() -> new
                ResourceNotFoundException(medicalScheduleRequestDTO.getDoctorId()));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw new AccessDeniedException("You do not have permission to create medical schedules.");

        MedicalSchedule medicalSchedule = new MedicalSchedule();

        medicalSchedule.setStarTime(medicalScheduleRequestDTO.getStarTime());
        medicalSchedule.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedule.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        medicalSchedule.setDaysOfWeek(medicalScheduleRequestDTO.getDaysOfWeek());
        medicalSchedule.setDoctor(doctor);
        medicalScheduleRepository.save(medicalSchedule);
        return new MedicalSheduleResponse(medicalSchedule);
    }

    public List<MedicalSheduleResponse> findAll() {
        List<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findAll();
        return medicalSchedules.stream().map(MedicalSheduleResponse::new).collect(Collectors.toList());
    }

    public MedicalSheduleResponse findById(Long id) {
        MedicalSchedule medicalSchedule = medicalScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new MedicalSheduleResponse(medicalSchedule);
    }

    public void deleteById(Long id , Authentication authentication) {

        medicalScheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw new AccessDeniedException("You do not have permission to delete medical schedules.");

        medicalScheduleRepository.deleteById(id);
    }

    public MedicalSheduleResponse update(Long id, MedicalScheduleRequest medicalSchedule, Authentication authentication){

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw new AccessDeniedException("You do not have permission to update medical schedules.");

        MedicalSchedule medicalScheduleEntity = medicalScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        updateDate(medicalScheduleEntity, medicalSchedule);
        medicalScheduleRepository.save(medicalScheduleEntity);
        return new MedicalSheduleResponse(medicalScheduleEntity);
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
