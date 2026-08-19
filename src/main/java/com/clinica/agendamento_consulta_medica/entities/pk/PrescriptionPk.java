package com.clinica.agendamento_consulta_medica.entities.pk;
import com.clinica.agendamento_consulta_medica.entities.Medications;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class PrescriptionPk {

    @ManyToOne
    @JoinColumn(name = "id_prescription")
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "id_medications")
    private Medications medications;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PrescriptionPk that = (PrescriptionPk) o;
        return Objects.equals(prescription, that.prescription) && Objects.equals(medications, that.medications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescription, medications);
    }
}
