package com.clinica.agendamento_consulta_medica.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String phone;

    @OneToMany(mappedBy = "doctors")
    private List<Specialty> specialties = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations = new ArrayList<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @OneToMany(mappedBy = "doctor")
    private Set<MedicalSchedule> medicalSchedules = new HashSet<>();

}

