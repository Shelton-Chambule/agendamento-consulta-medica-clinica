package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "tb_prescription")
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalDate validity;
    private String observations;

    @OneToOne
    @JoinColumn(name = "id_consultation")
    private Consultation consultation;

    @OneToMany(mappedBy = "prescription")
    private Set<Medications> medications = new HashSet<>();


    @Builder.Default
    @OneToMany(mappedBy = "id.prescription")
    private List<PrescriptionItem> prescriptionItem = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prescription receita = (Prescription) o;
        return Objects.equals(id, receita.id);
    }

    public List<PrescriptionItem> getPrescriptionItem() {
        return prescriptionItem;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
