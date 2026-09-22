package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PrescriptionResponse {

    private Long id;
    private LocalDate date;
    private String observations;
    private String dosagem;
    private String frequency;
    private Set<String> medications;

    public PrescriptionResponse(Prescription prescription) {
        id = prescription.getId();
        date = prescription.getDate();
        observations = prescription.getObservations();
        medications = prescription.getMedications();
        dosagem = prescription.getDosagem();
        frequency = prescription.getFrequency();
    }
}
