package com.clinica.agendamento_consulta_medica.entities;
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
    private LocalDate date;
    private String observations;
    private String dosagem;
    private String frequency;
    private Set<String> medications;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation ;

    public Prescription(Long id, LocalDate date, String observations, String dosagem, String frequency) {
        this.id = id;
        this.date = date;
        this.observations = observations;
        this.dosagem = dosagem;
        this.frequency = frequency;
    }

    @PrePersist
    public  void date(){
        this.date = LocalDate.now();
    }

}
