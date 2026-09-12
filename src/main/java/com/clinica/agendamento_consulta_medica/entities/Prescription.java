package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tb_prescription")
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String observations;
    private String dosagem;
    private String frequency;
    private Set<String> medications;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation ;

    public Prescription(){}

    public Prescription(Long id, LocalDate date, String observations, String dosagem, String frequency) {
        this.id = id;
        this.date = date;
        this.observations = observations;
        this.dosagem = dosagem;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prescription receita = (Prescription) o;
        return Objects.equals(id, receita.id);
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getObservations() {
        return observations;
    }

    @PrePersist
    public  void date(){
        this.date = LocalDate.now();
    }

}
