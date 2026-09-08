package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "tb_doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    private String name;
    private String email;
    private String phone;
    private String password;


    @ManyToMany
    @JoinTable(name = "tb_especialty_doctor", joinColumns = @JoinColumn(name = "id_doctor"), inverseJoinColumns = @JoinColumn(name = "id_specialty"))
    private List<Specialty> specialties = new ArrayList<>();


    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations = new ArrayList<>();


    @OneToMany(mappedBy = "doctor")
    private Set<MedicalSchedule> medicalSchedules = new HashSet<>();

    public Doctor(){}

    public Doctor(Long doctorId, String name, String email, String phone, String password) {
        this.doctorId = doctorId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public Set<MedicalSchedule> getMedicalSchedules() {
        return medicalSchedules;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(doctorId);
    }


    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
