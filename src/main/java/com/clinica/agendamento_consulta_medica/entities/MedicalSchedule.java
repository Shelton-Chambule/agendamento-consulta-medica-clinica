package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_hours_Doctor")
public class MedicalSchedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> dayOfWeek = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MedicalSchedule that = (MedicalSchedule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
