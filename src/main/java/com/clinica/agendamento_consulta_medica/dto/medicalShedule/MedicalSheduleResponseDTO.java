package com.clinica.agendamento_consulta_medica.dto.medicalShedule;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.entities.enums.DaysWeeks;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalSheduleResponseDTO {

    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;
    private Long doctorId;
    private List<String> daysOfWeek;

    public MedicalSheduleResponseDTO(){}

    public MedicalSheduleResponseDTO(MedicalSchedule medicalSchedule) {
        this.id = medicalSchedule.getId();
        this.starTime = medicalSchedule.getStarTime();
        this.endTime = medicalSchedule.getEndTime();
        this.breakTimes =  medicalSchedule.getBreakTimes();
        this.doctorId = medicalSchedule.getDoctor().getDoctorId();
        this.daysOfWeek  = medicalSchedule.getDaysOfWeek().stream()
                .map(DaysWeeks::name)
                .collect(Collectors.toList());
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getBreakTimes() {
        return breakTimes;
    }

    public void setBreakTimes(LocalTime breakTimes) {
        this.breakTimes = breakTimes;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
