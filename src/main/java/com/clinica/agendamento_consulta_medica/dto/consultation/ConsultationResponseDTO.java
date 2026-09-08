package com.clinica.agendamento_consulta_medica.dto.consultation;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConsultationResponseDTO {

    private Long Id;
    private LocalDateTime moment;
    private LocalDate date;
    private LocalTime starTime;
    private Integer duration;
    private Long patient;
    private Long doctor;
    private StatusConsultation statusConsultation;

    public ConsultationResponseDTO(){}

    public ConsultationResponseDTO(Consultation consultation) {
        Id = consultation.getId();
        this.moment  = consultation.getMoment();
        this.date =consultation.getDate() ;
        this.starTime = consultation.getStarTime();
        this.duration = consultation.getDuration();
        this.patient = consultation.getPatient().getPatientId();
        this.doctor = consultation.getDoctor().getDoctorId();
        this.statusConsultation =  consultation.getStatusConsultation();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public void setMoment(LocalDateTime moment) {
        this.moment = moment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getPatient() {
        return patient;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getDoctor() {
        return doctor;
    }

    public void setDoctor(Long doctor) {
        this.doctor = doctor;
    }

    public StatusConsultation getStatusConsultation() {
        return statusConsultation;
    }

    public void setStatusConsultation(StatusConsultation statusConsultation) {
        this.statusConsultation = statusConsultation;
    }
}
