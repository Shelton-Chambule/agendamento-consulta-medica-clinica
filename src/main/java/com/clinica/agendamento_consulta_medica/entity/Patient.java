package com.clinica.agendamento_consulta_medica.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_patient")
@EqualsAndHashCode(of = "patientId")
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "patient")
    private List<Consultation> consultations = new ArrayList<>();

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
}
