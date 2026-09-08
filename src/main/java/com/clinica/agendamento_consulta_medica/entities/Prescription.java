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
    private LocalDate validity;
    private String observations;

    @OneToOne
    @JoinColumn(name = "id_consultation")
    private Consultation consultation;

    @OneToMany(mappedBy = "prescription")
    private Set<Medications> medications = new HashSet<>();

    @OneToMany(mappedBy = "id.prescription")
    private List<PrescriptionItem> prescriptionItem = new ArrayList<>();

    public Prescription(){}

    public Prescription(Long id, LocalDate date, LocalDate validity, String observations, Consultation consultation) {
        this.id = id;
        this.date = date;
        this.validity = validity;
        this.observations = observations;
        this.consultation = consultation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prescription receita = (Prescription) o;
        return Objects.equals(id, receita.id);
    }

    public List<PrescriptionItem> getPrescriptionItem() {
        return prescriptionItem;
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

    public void setValidity(LocalDate validity) {
        this.validity = validity;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getValidity() {
        return validity;
    }

    public String getObservations() {
        return observations;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public Set<Medications> getMedications() {
        return medications;
    }
}
