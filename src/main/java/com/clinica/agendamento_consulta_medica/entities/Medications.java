package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "tb_medications")
public class Medications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "id.medications")
    private List<PrescriptionItem>  prescriptionItem = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_prescription")
    private Prescription prescription;

    public Medications(){}

    public Medications(Prescription prescription, String name, Long id) {
        this.prescription = prescription;
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Medications that = (Medications) o;
        return Objects.equals(id, that.id);
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PrescriptionItem> getPrescriptionItem() {
        return prescriptionItem;
    }

    public Prescription getPrescription() {
        return prescription;
    }
}
