package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_hours_Doctor")
public class MedicalSchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;

    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> dayOfWeek = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    public MedicalSchedule(){}

    public MedicalSchedule(Long id, LocalTime starTime, LocalTime endTime, LocalTime breakTimes, Set<DayOfWeek> dayOfWeek, Doctor doctor) {
        this.id = id;
        this.starTime = starTime;
        this.endTime = endTime;
        this.breakTimes = breakTimes;
        this.dayOfWeek = dayOfWeek;
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MedicalSchedule that = (MedicalSchedule) o;
        return Objects.equals(id, that.id);
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

    public Set<DayOfWeek> getDayOfWeek() {
        return dayOfWeek;
    }

    public Doctor getDoctor() {
        return doctor;
    }

}
