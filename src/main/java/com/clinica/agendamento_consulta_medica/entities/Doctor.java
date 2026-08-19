package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "tb_especialty_doctor", joinColumns = @JoinColumn(name = "id_doctor"), inverseJoinColumns = @JoinColumn(name = "id_specialty"))
    private List<Specialty> specialties = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "doctor")
    private Set<MedicalSchedule> medicalSchedules = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(doctorId);
    }

}
