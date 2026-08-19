package com.clinica.agendamento_consulta_medica.dto.prescriptionItem;
import com.clinica.agendamento_consulta_medica.entities.PrescriptionItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
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
}
