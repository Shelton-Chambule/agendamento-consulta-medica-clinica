package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequestDTO;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponseDTO;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.entities.enums.DaysWeeks;
import com.clinica.agendamento_consulta_medica.repository.DoctorRepository;
import com.clinica.agendamento_consulta_medica.repository.MedicalScheduleRepository;
import com.clinica.agendamento_consulta_medica.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicalScheduleService {

    private final  MedicalScheduleRepository medicalScheduleRepository;
    private final DoctorRepository doctorRepository;

    public MedicalScheduleService(MedicalScheduleRepository medicalScheduleRepository, DoctorRepository doctorRepository) {
        this.medicalScheduleRepository = medicalScheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    public MedicalSheduleResponseDTO save(MedicalScheduleRequestDTO medicalScheduleRequestDTO) {
        Doctor doctor = doctorRepository.findById(medicalScheduleRequestDTO.getDoctorId()).orElseThrow(() -> new
                ResourceNotFoundException(medicalScheduleRequestDTO.getDoctorId()));

        MedicalSchedule medicalSchedule = new MedicalSchedule();

        medicalSchedule.setStarTime(medicalScheduleRequestDTO.getStarTime());
        medicalSchedule.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedule.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        Set<DaysWeeks> days = medicalScheduleRequestDTO.getDaysOfWeek().stream().flatMap(s -> Arrays.stream(s.split(","))).map(String::trim).map(String::toUpperCase).map(DaysWeeks::valueOf).collect(Collectors.toSet());
        medicalSchedule.setDaysOfWeek(days);
        medicalSchedule.setDoctor(doctor);
        medicalScheduleRepository.save(medicalSchedule);
        return new MedicalSheduleResponseDTO(medicalSchedule);
    }

    public List<MedicalSheduleResponseDTO> findAll() {
        List<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findAll();
        return medicalSchedules.stream().map(MedicalSheduleResponseDTO::new).collect(Collectors.toList());
    }

    public MedicalSheduleResponseDTO findById(Long id) {
        Optional<MedicalSchedule> medicalSchedule = medicalScheduleRepository.findById(id);
        return new MedicalSheduleResponseDTO(medicalSchedule.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {

        if(!medicalScheduleRepository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }

        medicalScheduleRepository.deleteById(id);
    }

    public MedicalSheduleResponseDTO update(Long id, MedicalScheduleRequestDTO medicalSchedule){
        try{
            MedicalSchedule medicalSchedules = medicalScheduleRepository.getReferenceById(id);
            updateDate(medicalSchedules,medicalSchedule);
            medicalScheduleRepository.save(medicalSchedules);
            return new MedicalSheduleResponseDTO(medicalSchedules);
        }catch (ResourceNotFoundException exception){
            throw new IllegalArgumentException("Error while search id, verify if exists id");
        }
    }

    private void updateDate(MedicalSchedule medicalSchedules, MedicalScheduleRequestDTO medicalScheduleRequestDTO) {
        medicalSchedules.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedules.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        medicalSchedules.setStarTime(medicalScheduleRequestDTO.getStarTime());
    }
}
