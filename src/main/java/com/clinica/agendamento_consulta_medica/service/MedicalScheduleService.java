package com.clinica.agendamento_consulta_medica.service;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleDto;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.repository.MedicalScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalScheduleService {


    private final  MedicalScheduleRepository medicalScheduleRepository;

    public MedicalScheduleService(MedicalScheduleRepository medicalScheduleRepository) {
        this.medicalScheduleRepository = medicalScheduleRepository;
    }

    public MedicalScheduleDto save(MedicalScheduleDto medicalSchedule) {
        MedicalSchedule medicalSchedule1 = new MedicalSchedule();
        medicalSchedule1.setId(medicalSchedule.getId());
        medicalSchedule1.setStarTime(medicalSchedule.getStarTime());
        medicalSchedule1.setEndTime(medicalSchedule.getEndTime());
        medicalScheduleRepository.save(medicalSchedule1);
        return new MedicalScheduleDto(medicalSchedule1);
    }

    public List<MedicalScheduleDto> findAll() {
        List<MedicalSchedule> medicalSchedules = medicalScheduleRepository.findAll();
        return medicalSchedules.stream().map(MedicalScheduleDto::new).collect(Collectors.toList());
    }

    public MedicalScheduleDto findById(Long id) {
        Optional<MedicalSchedule> medicalSchedule = medicalScheduleRepository.findById(id);
        return new MedicalScheduleDto(medicalSchedule.get());
    }

    public void deleteById(Long id) {
        medicalScheduleRepository.deleteById(id);
    }

    public MedicalScheduleDto update(Long id, MedicalScheduleDto medicalSchedule){
        MedicalSchedule medicalSchedules = medicalScheduleRepository.getReferenceById(id);
        updateDate(medicalSchedules,medicalSchedule);
         medicalScheduleRepository.save(medicalSchedules);
         return new MedicalScheduleDto(medicalSchedules);
    }

    private void updateDate(MedicalSchedule medicalSchedules, MedicalScheduleDto medicalSchedule) {
        medicalSchedules.setEndTime(medicalSchedule.getEndTime());
        medicalSchedules.setStarTime(medicalSchedule.getStarTime());
    }
}
