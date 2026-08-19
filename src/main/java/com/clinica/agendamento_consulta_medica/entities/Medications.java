package com.clinica.agendamento_consulta_medica.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_medications")
public class Medications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "id.medications")
    private List<PrescriptionItem>  prescriptionItem = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_prescription")
    private Prescription prescription;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Medications that = (Medications) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
