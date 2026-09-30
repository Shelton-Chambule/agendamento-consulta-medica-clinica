package com.clinica.agendamento_consulta_medica.dto.prescrition;
import com.clinica.agendamento_consulta_medica.entity.Prescription;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(required = true)
    private LocalDate date;

    @NotBlank
    @JsonProperty(required = true)
    private String observations;

    @NotBlank
    @JsonProperty(required = true)
    private String dosage;

    @NotBlank
    @JsonProperty(required = true)
    private String frequency;

    @JsonProperty(required = true)
    private Set<String> medications;

    @NotNull(message = "Required field")
    @NotBlank
    private Long consultationId;

    public PrescriptionRequest(Prescription prescription) {
        date = prescription.getDate();
        observations = prescription.getObservations();
        dosage = prescription.getDosage();
        frequency = prescription.getFrequency();
        medications = prescription.getMedications();
        consultationId = prescription.getConsultation().getId();
    }
}
