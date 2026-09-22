package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "tb_history_patient")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class HistoryPatient {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "consultation_Id")
    private Consultation consultation;

    @Enumerated(EnumType.STRING)
    private StatusConsultation statusConsultation;
}
