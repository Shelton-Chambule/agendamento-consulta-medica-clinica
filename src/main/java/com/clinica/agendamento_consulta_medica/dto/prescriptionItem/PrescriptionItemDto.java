package com.clinica.agendamento_consulta_medica.dto.prescriptionItem;
import com.clinica.agendamento_consulta_medica.entities.PrescriptionItem;
public class PrescriptionItemDto {

    private String medicationName;
    private Long prescriptionId;
    private String dosagem;
    private String frequency;

    public PrescriptionItemDto(PrescriptionItem prescriptionItem) {
        medicationName = prescriptionItem.getMedications().getName();
        prescriptionId = prescriptionItem.getPrescription().getId();
        dosagem = prescriptionItem.getDosagem();
        frequency = prescriptionItem.getFrequency();
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
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
