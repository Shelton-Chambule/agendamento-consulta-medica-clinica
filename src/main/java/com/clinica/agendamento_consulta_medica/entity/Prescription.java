package com.clinica.agendamento_consulta_medica.entity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tb_prescription")
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "observations",nullable = false)
    private String observations;

    @Column(name = "dosage",nullable = false)
    private String dosage;

    @Column(name = "frequency",nullable = false)
    private String frequency;

    @Column(name = "medications", nullable = false)
    private Set<String> medications;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation ;

    public Prescription(Long id, LocalDate date, String observations, String dosage, String frequency) {
        this.id = id;
        this.date = date;
        this.observations = observations;
        this.dosage = dosage;
        this.frequency = frequency;
    }

    @PrePersist
    public  void date(){
        this.date = LocalDate.now();
    }

}
