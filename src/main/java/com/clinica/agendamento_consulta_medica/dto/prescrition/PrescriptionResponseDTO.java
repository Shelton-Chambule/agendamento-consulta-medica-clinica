package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import java.time.LocalDate;
import java.util.Set;
public class PrescriptionResponseDTO {

    private Long id;
    private LocalDate date;
    private String observations;
    private String dosagem;
    private String frequency;
    private Set<String> medications;

    public PrescriptionResponseDTO(){}

    public PrescriptionResponseDTO(Prescription prescription) {
        id = prescription.getId();
        date = prescription.getDate();
        observations = prescription.getObservations();
        medications = prescription.getMedications();
        dosagem = prescription.getDosagem();
        frequency = prescription.getFrequency();
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

    public Set<String> getMedications() {
        return medications;
    }

    public void setMedications(Set<String> medications) {
        this.medications = medications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
