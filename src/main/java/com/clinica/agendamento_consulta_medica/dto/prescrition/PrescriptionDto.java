package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.dto.prescriptionItem.PrescriptionItemDto;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
public class PrescriptionDto {

    private Long id;
    private LocalDate date;
    private LocalDate validity;
    private String observations;
    private List<PrescriptionItemDto> prescriptionItem;

    public  PrescriptionDto(Prescription prescription){
        id = prescription.getId();
        date = prescription.getDate();
        validity = prescription.getValidity();
        observations = prescription.getObservations();
        prescriptionItem = prescription.getPrescriptionItem().stream().map(PrescriptionItemDto::new).collect(Collectors.toList());
    }
}
