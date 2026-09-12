package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_consultation")
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime moment;
    private LocalDate date;
    private LocalTime starTime;
    private Integer duration;

    @OneToMany(mappedBy = "consultation")
    private Set<Prescription> revenue;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private StatusConsultation statusConsultation;

    @OneToOne(mappedBy = "consultations")
    private HistoryPatient historyPatient;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    public Consultation(){}

    public Consultation(Long id, LocalDateTime moment, LocalDate date, LocalTime starTime, Integer duration, StatusConsultation statusConsultation) {
        this.id = id;
        this.moment = moment;
        this.date = date;
        this.starTime = starTime;
        this.duration = duration;
        this.statusConsultation = statusConsultation;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public StatusConsultation getStatusConsultation() {
        return statusConsultation;
    }

    public void setStatusConsultation(StatusConsultation statusConsultation) {
        this.statusConsultation = statusConsultation;
    }

    public HistoryPatient getHistoryPatient() {
        return historyPatient;
    }

    public void setHistoryPatient(HistoryPatient historyPatient) {
        this.historyPatient = historyPatient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMoment(LocalDateTime moment) {
        this.moment = moment;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public Integer getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Consultation that = (Consultation) o;
        return Objects.equals(id, that.id);
    }

    public Set<Prescription> getRevenue() {
        return revenue;
    }

    public void setRevenue(Set<Prescription> revenue) {
        this.revenue = revenue;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @PrePersist
    public void moment() {
        this.moment = LocalDateTime.now();
    }

//    @PrePersist
//    public void date() {
//        this.date = LocalDate.now();
//    }

}
