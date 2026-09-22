package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "tb_medical_schedule")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class MedicalSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;
    private List<String> daysOfWeek;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

}
