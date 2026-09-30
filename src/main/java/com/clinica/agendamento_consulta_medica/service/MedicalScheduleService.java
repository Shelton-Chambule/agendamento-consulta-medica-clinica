package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import com.clinica.agendamento_consulta_medica.entity.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.exception.AccessDeniedException;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.MedicalScheduleRepository;
import com.clinica.agendamento_consulta_medica.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalScheduleService {

    private final  MedicalScheduleRepository medicalScheduleRepository;
    private final DoctorRepository doctorRepository;

    public MedicalScheduleService(MedicalScheduleRepository medicalScheduleRepository, DoctorRepository doctorRepository) {
        this.medicalScheduleRepository = medicalScheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    public MedicalSheduleResponse save(MedicalScheduleRequest medicalScheduleRequestDTO, Authentication authentication) {

        Doctor doctor = doctorRepository.findById(medicalScheduleRequestDTO.getDoctorId()).orElseThrow(() -> new
                ResourceNotFoundException(medicalScheduleRequestDTO.getDoctorId()));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw  new AccessDeniedException("Not authorization to this access");

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

    public MedicalSheduleResponse findById(Long id, Authentication authentication) {

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw  new AccessDeniedException("Not authorization to this access");

        Optional<MedicalSchedule> medicalSchedule = medicalScheduleRepository.findById(id);
        return new MedicalSheduleResponse(medicalSchedule.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id , Authentication authentication) {

        medicalScheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw  new AccessDeniedException("Not authorization to this access");

        medicalScheduleRepository.deleteById(id);
    }

    public MedicalSheduleResponse update(Long id, MedicalScheduleRequest medicalSchedule, Authentication authentication){

        boolean admin = authentication.getAuthorities().stream().anyMatch(any -> any.getAuthority().equals("ROLE_ADMIN"));

        if(!admin) throw  new AccessDeniedException("Not authorization to this access");

        try{
            MedicalSchedule medicalSchedules = medicalScheduleRepository.getReferenceById(id);
            updateDate(medicalSchedules,medicalSchedule);
            medicalScheduleRepository.save(medicalSchedules);
            return new MedicalSheduleResponse(medicalSchedules);
        }catch (ResourceNotFoundException exception){
            throw new IllegalArgumentException("Error while search id, verify if exists id");
        }
    }

    private void updateDate(MedicalSchedule medicalSchedules, MedicalScheduleRequest medicalScheduleRequestDTO) {
        medicalSchedules.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedules.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        medicalSchedules.setStarTime(medicalScheduleRequestDTO.getStarTime());
        medicalSchedules.setDaysOfWeek(medicalScheduleRequestDTO.getDaysOfWeek());
    }
}
