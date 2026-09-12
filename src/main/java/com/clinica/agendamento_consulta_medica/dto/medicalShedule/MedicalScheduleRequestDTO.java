package com.clinica.agendamento_consulta_medica.dto.medicalShedule;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.entities.enums.DaysWeeks;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;
public class MedicalScheduleRequestDTO {

    @JsonFormat(pattern = "H:mm:ss")
    private LocalTime starTime;
    @JsonFormat(pattern = "H:mm:ss")
    private LocalTime endTime;
    @JsonFormat(pattern = "H:mm:ss")
    private LocalTime breakTimes;

    private List<String> daysOfWeek;

    private Long doctorId;

    public MedicalScheduleRequestDTO(){}

    public MedicalScheduleRequestDTO(MedicalSchedule medicalSchedule) {
        starTime = medicalSchedule.getStarTime();
        endTime = medicalSchedule.getEndTime();
        doctorId = medicalSchedule.getDoctor().getDoctorId();
        breakTimes = medicalSchedule.getBreakTimes();
        daysOfWeek = medicalSchedule.getDaysOfWeek().stream().map(DaysWeeks::name).toList();
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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
