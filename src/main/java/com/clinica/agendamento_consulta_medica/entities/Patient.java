package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    private String name;
    private String email;
    private String phone;
    private String password;

    @OneToMany(mappedBy = "patient")
    private List<Consultation> consultations = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }

    public Patient(){}

    public Patient(Long patientId, String name, String email, String phone, String password) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(patientId);
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }
}
