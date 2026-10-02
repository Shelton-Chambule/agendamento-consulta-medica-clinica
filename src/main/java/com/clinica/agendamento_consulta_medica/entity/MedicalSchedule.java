package com.clinica.agendamento_consulta_medica.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false)
    private LocalTime starTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalTime breakTimes;

    @ElementCollection
    @CollectionTable(name = "tb_medical_schedule_day", joinColumns = @JoinColumn(name = "medical_schedule_id"))
    @Column(name = "day_of_week", nullable = false)
    private List<String> daysOfWeek;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

}
