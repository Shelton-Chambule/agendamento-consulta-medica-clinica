package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "tb_doctor")
@EqualsAndHashCode(of = "doctorId")
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    private String name;
    private String phone;

    @OneToMany(mappedBy = "doctors")
    private List<Specialty> specialties = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations = new ArrayList<>();

    @OneToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @OneToMany(mappedBy = "doctor")
    private Set<MedicalSchedule> medicalSchedules = new HashSet<>();

}

