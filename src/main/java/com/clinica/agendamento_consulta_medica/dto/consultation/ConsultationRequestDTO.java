package com.clinica.agendamento_consulta_medica.dto.consultation;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class ConsultationRequestDTO {

    private LocalDateTime moment;

    @JsonFormat(pattern ="yyyy-MM-dd")
    @NotNull(message = "date not can be null")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "the starTime not can be null")
    private LocalTime starTime;

    @NotNull(message = "the duration not can be null")
    private Integer duration;

    @NotNull(message = "the id patient not can be null")
    private Long patient;

    @NotNull(message = "the id doctor not can be null")
    private Long doctor;

    private StatusConsultation statusConsultation;

    public ConsultationRequestDTO(){}

    public ConsultationRequestDTO(Consultation consultation) {
        moment = consultation.getMoment();
        date = consultation.getDate();
        starTime = consultation.getStarTime();
        duration = consultation.getDuration();
        patient = consultation.getPatient().getPatientId();
        statusConsultation = consultation.getStatusConsultation();
        doctor = consultation.getDoctor().getDoctorId();
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
