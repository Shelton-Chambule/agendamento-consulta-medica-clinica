package com.clinica.agendamento_consulta_medica.entity;
import com.clinica.agendamento_consulta_medica.entity.enums.StatusConsultation;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class Consultation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime moment;

<<<<<<< Updated upstream:src/main/java/com/clinica/agendamento_consulta_medica/entity/Consultation.java
    @Column()
=======
    @Column(nullable = false)
>>>>>>> Stashed changes:src/main/java/com/clinica/agendamento_consulta_medica/entities/Consultation.java
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
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

    @PrePersist
    public void moment() {
        this.moment = LocalDateTime.now();
    }

}
