package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(required = true)
    private String name;

    @Email(message = "format email invalid")
    @NotBlank
    @JsonProperty(required = true)
    private String login;

    @NotBlank
    @JsonProperty(required = true)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "The password must contain an uppercase letter, a lowercase letter, a number, and a special character."
    )
    private String password;

    @NotBlank(message = "Required field")
    @Size(min = 9, max = 12)
    @JsonProperty(required = true)
    private String phone;

    @NotNull
    @Past(message = "The date cannot future or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(required = true)
    private LocalDate dataNascimento;

    public PatientRequest(Patient patient) {
        name = patient.getName();
        phone = patient.getPhone();
        dataNascimento = patient.getDataNascimento();
        login = patient.getAccount().getLogin();
        password = patient.getAccount().getPassword();
    }

}
