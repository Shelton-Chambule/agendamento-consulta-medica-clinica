package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SpecialtyRequest {

    @NotBlank(message = "Required field")
    private String name;

    @NotNull(message = "Required field")
    private Long doctorId;

    public SpecialtyRequest(Specialty specialty) {
        name = specialty.getName();
        doctorId = specialty.getDoctors().getDoctorId();
    }
}
