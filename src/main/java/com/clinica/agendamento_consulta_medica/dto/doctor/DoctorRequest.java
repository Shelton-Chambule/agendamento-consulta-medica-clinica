package com.clinica.agendamento_consulta_medica.dto.doctor;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    @Email(message = "Login must be a valid email address.")
    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "Password must contain uppercase and lowercase letters, a number, and a special character.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 9, max = 12)
    private String phone;

    public DoctorRequest(Doctor doctor) {
        name = doctor.getName();
        phone = doctor.getPhone();
        login = doctor.getAccount().getLogin();
        password = doctor.getAccount().getPassword();
    }

}
