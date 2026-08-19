package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_consultation")
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consultation")
    private Long id;

    @Column(name = "moment")
    private Instant moment;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "startTime")
    private LocalTime starTime;

    @Column(name = "duration")
    private Integer duration;

    @OneToOne(mappedBy = "consultation")
    private Prescription revenue;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private StatusConsultation statusConsultation;

    @OneToOne(mappedBy = "consultations")
    private HistoryPatient historyPatient;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Consultation that = (Consultation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
