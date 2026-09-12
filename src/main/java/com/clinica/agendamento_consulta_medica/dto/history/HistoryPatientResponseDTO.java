package com.clinica.agendamento_consulta_medica.dto.history;
import com.clinica.agendamento_consulta_medica.entities.HistoryPatient;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import java.time.LocalDate;
import java.time.LocalTime;
public class HistoryPatientResponseDTO {

    private Long historyId;
    private Long patientId;
    private String namePatient;
    private LocalTime statTime;
    private LocalDate date;
    private Integer duration;
    private StatusConsultation statusConsultation;

    public HistoryPatientResponseDTO(HistoryPatient historyPatient) {
        historyId = historyPatient.getId();
        patientId =  historyPatient.getConsultations().getPatient().getPatientId();
        namePatient = historyPatient.getConsultations().getPatient().getName();
        statTime = historyPatient.getConsultations().getStarTime();
        duration = historyPatient.getConsultations().getDuration();
        date = historyPatient.getConsultations().getDate();
        statusConsultation  = historyPatient.getStatusConsultation();
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long id) {
        this.historyId = id;
    }

    public Long getIPatientId() {
        return patientId;
    }

    public void setPatientId(Long idPatient) {
        this.patientId = idPatient;
    }

    public String getNamePatient() {
        return namePatient;
    }

    public void setNamePatient(String namePatient) {
        this.namePatient = namePatient;
    }

    public LocalTime getStatTime() {
        return statTime;
    }

    public void setStatTime(LocalTime statTime) {
        this.statTime = statTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public StatusConsultation getStatusConsultation() {
        return statusConsultation;
    }

    public void setStatusConsultation(StatusConsultation statusConsultation) {
        this.statusConsultation = statusConsultation;
    }
}
