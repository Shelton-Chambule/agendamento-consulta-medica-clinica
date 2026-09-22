package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
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
    private Long patientId;
    private String name;
    private String phone;
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "patient")
    private List<Consultation> consultations = new ArrayList<>();

    @OneToOne(optional = false)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
}
