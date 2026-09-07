package com.clinica.agendamento_consulta_medica.entities;
import com.clinica.agendamento_consulta_medica.entities.pk.PrescriptionPk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "tb_prescription_item")
public class PrescriptionItem implements Serializable {

    @EmbeddedId
    private PrescriptionPk id = new PrescriptionPk();
    private String dosagem;
    private String frequency;

    public PrescriptionItem(Prescription prescription , Medications medications , String dosagem , String frequency) {
        id.setPrescription(prescription);
        id.setMedications( medications);
        this.dosagem = dosagem;
        this.frequency = frequency;
    }

    public Prescription getPrescription() {
        return id.getPrescription();
    }
    public void setPrescription(Prescription prescription){
        id.setPrescription(prescription);
    }
    public Medications getMedications() {
        return id.getMedications();
    }
    public void setMedications(Medications medications){
        id.setMedications(medications);
    }


    public PrescriptionPk getId() {
        return id;
    }

    public void setId(PrescriptionPk id) {
        this.id = id;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
