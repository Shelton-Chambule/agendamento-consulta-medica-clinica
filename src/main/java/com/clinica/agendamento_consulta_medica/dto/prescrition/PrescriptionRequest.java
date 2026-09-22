package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.entities.Prescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PrescriptionRequest {

    private LocalDate date;

    @NotNull(message = "Required field")
    @NotBlank
    private String observations;

    @NotNull(message = "Required field")
    @NotBlank
    private String dosagem;

    @NotBlank
    @NotNull(message = "Required field")
    private String frequency;

    private Set<String> medications;

    @NotNull(message = "Required field")
    @NotBlank
    private Long consultationId;

    public PrescriptionRequest(Prescription prescription) {
        date = prescription.getDate();
        observations = prescription.getObservations();
        dosagem = prescription.getDosagem();
        frequency = prescription.getFrequency();
        medications = prescription.getMedications();
        consultationId = prescription.getConsultation().getId();
    }
}
