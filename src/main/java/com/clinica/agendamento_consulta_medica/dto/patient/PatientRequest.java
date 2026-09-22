package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import com.clinica.agendamento_consulta_medica.entities.enums.AccountRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class PatientRequest {

    @NotBlank(message = "Required field")
    private String name;

    @Email
    @NotBlank(message = "Required field")
    private String login;

    @NotBlank(message = "Required field")
    private String password;

    @NotBlank(message = "Required field")
    @Size(min = 9, max = 12)
    private String phone;

    @NotNull
    @Past(message = "The date cannot future or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    public PatientRequest(Patient patient) {
        name = patient.getName();
        phone = patient.getPhone();
        dataNascimento = patient.getDataNascimento();
        login = patient.getAccount().getLogin();
        password = patient.getAccount().getPassword();
    }

}
