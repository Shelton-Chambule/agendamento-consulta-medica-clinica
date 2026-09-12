package com.clinica.agendamento_consulta_medica.dto.doctor;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class DoctorRequestDTO {

    @NotBlank(message = "name not can it is  null")
    private String name;

    @Email
    private String email;

    @NotBlank(message = "cannot null")
    @Size(min = 9, max = 12)
    private String phone;

    public DoctorRequestDTO(){}

    public DoctorRequestDTO(Doctor doctor) {
        name = doctor.getName();
        email = doctor.getEmail();
        phone = doctor.getPhone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
