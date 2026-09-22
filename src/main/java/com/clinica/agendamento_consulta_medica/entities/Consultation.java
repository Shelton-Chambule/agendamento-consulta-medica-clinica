package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_consultation")
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime moment;
    private LocalDate date;
    private LocalTime startTime;
    private Duration duration;

    @OneToMany(mappedBy = "consultation")
    private Set<Prescription> revenue;

    @ManyToOne
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private StatusConsultation statusConsultation;

    @OneToOne(mappedBy = "consultation")
    private HistoryPatient historyPatient;

    @ManyToOne
    @JoinColumn(name = "id_patient")
    private Patient patient;

    public Consultation(){}

    public Consultation(Long id, LocalDateTime moment, LocalDate date, LocalTime startTime, Duration duration, StatusConsultation statusConsultation) {
        this.id = id;
        this.moment = moment;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.statusConsultation = statusConsultation;
    }

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

    @PrePersist
    public void moment() {
        this.moment = LocalDateTime.now();
    }

}
