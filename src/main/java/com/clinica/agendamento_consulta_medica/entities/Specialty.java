package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@Table(name = "tb_specialty")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

   @ManyToOne
   @JoinColumn(name = "id_doctor")
    private Doctor doctors;

    public Specialty(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
    }
}
