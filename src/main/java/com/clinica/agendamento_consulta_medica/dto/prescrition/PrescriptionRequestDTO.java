package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
public class PrescriptionRequestDTO {

    private LocalDate date;

    @NotNull(message = "Required field")
    private String observations;

    @NotNull(message = "Required field")
    private String dosagem;

    @NotNull(message = "Required field")
    private String frequency;

    private Set<String> medications;

    @NotNull(message = "Required field")
    private Long consultationId;

    public PrescriptionRequestDTO(){}

    public PrescriptionRequestDTO(Prescription prescription) {
        date = prescription.getDate();
        observations = prescription.getObservations();
        dosagem = prescription.getDosagem();
        frequency = prescription.getFrequency();
        medications = prescription.getMedications();
        consultationId = prescription.getConsultation().getId();
    }

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public Set<String> getMedications() {
        return medications;
    }

    public void setMedications(Set<String> medications) {
        this.medications = medications;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
