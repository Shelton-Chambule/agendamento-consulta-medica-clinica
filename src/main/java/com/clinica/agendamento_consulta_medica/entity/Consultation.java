package com.clinica.agendamento_consulta_medica.entity;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "tb_consultation")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "moment")
    private LocalDateTime moment;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "duration", nullable = false)
    private Duration duration;

    @Column(name = "Consultation description", nullable = false)
    private String consultationDescription;

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

    public Consultation(Long id, LocalDateTime moment, LocalDate date, LocalTime startTime, Duration duration, StatusConsultation statusConsultation, String  consultationDescription) {
        this.id = id;
        this.moment = moment;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        this.statusConsultation = statusConsultation;
        this.consultationDescription = consultationDescription;
    }

    @PrePersist
    public void moment() {
        this.moment = LocalDateTime.now();
    }

}
