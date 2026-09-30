package com.clinica.agendamento_consulta_medica.entity;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
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
    @Column(name = "history_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "consultation_Id")
    private Consultation consultation;

    @Enumerated(EnumType.STRING)
    private StatusConsultation statusConsultation;
}
