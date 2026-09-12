package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.DaysWeeks;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "tb_medical_schedule")
public class MedicalSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;

    @ElementCollection(targetClass = DaysWeeks.class)
    @CollectionTable(name = "medical_schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(EnumType.STRING)
    private Set<DaysWeeks> daysOfWeek = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    public MedicalSchedule(){}

    public MedicalSchedule(Long id, LocalTime starTime, LocalTime endTime, LocalTime breakTimes, Set<DaysWeeks> daysOfWeek, Doctor doctor) {
        this.id = id;
        this.starTime = starTime;
        this.endTime = endTime;
        this.breakTimes = breakTimes;
        this.daysOfWeek = daysOfWeek;
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MedicalSchedule that = (MedicalSchedule) o;
        return Objects.equals(id, that.id);
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setBreakTimes(LocalTime breakTimes) {
        this.breakTimes = breakTimes;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getBreakTimes() {
        return breakTimes;
    }

    public Set<DaysWeeks> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DaysWeeks> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Doctor getDoctor() {
        return doctor;
    }

}
