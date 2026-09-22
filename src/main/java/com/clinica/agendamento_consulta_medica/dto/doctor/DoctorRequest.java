package com.clinica.agendamento_consulta_medica.dto.doctor;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "name not can it is  null")
    private String name;

    @Email
    @NotBlank(message = "Required field")
    private String login;

    @NotBlank(message = "Required field")
    private String password;

    @NotBlank(message = "cannot null")
    @Size(min = 9, max = 12)
    private String phone;

    public DoctorRequest(Doctor doctor) {
        name = doctor.getName();
        phone = doctor.getPhone();
        login = doctor.getAccount().getLogin();
        password = doctor.getAccount().getPassword();
    }

}
