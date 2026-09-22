package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
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

    public MedicalSheduleResponse save(MedicalScheduleRequest medicalScheduleRequestDTO) {
        Doctor doctor = doctorRepository.findById(medicalScheduleRequestDTO.getDoctorId()).orElseThrow(() -> new
                ResourceNotFoundException(medicalScheduleRequestDTO.getDoctorId()));

        MedicalSchedule medicalSchedule = new MedicalSchedule();

        medicalSchedule.setStarTime(medicalScheduleRequestDTO.getStarTime());
        medicalSchedule.setEndTime(medicalScheduleRequestDTO.getEndTime());
        medicalSchedule.setBreakTimes(medicalScheduleRequestDTO.getBreakTimes());
        medicalSchedule.setDoctor(doctor);
        medicalScheduleRepository.save(medicalSchedule);
        return new MedicalSheduleResponse(medicalSchedule);
    }

    public List<MedicalSheduleResponse> findAll() {
        List<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findAll();
        return medicalSchedules.stream().map(MedicalSheduleResponse::new).collect(Collectors.toList());
    }

    public MedicalSheduleResponse findById(Long id) {
        Optional<MedicalSchedule> medicalSchedule = medicalScheduleRepository.findById(id);
        return new MedicalSheduleResponse(medicalSchedule.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    public void deleteById(Long id) {

        if(!medicalScheduleRepository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }

        medicalScheduleRepository.deleteById(id);
    }

    public MedicalSheduleResponse update(Long id, MedicalScheduleRequest medicalSchedule){
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
    }
}
