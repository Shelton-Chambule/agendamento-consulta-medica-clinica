package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class PatientRequestDTO {

    @NotBlank(message = "Required field")
    private String name;

    @Email
    private String email;

    @NotBlank(message = "Required field")
    @Size(min = 9, max = 12)
    private String phone;

    public PatientRequestDTO(){}

    public PatientRequestDTO(Patient patient) {
        name = patient.getName();
        email = patient.getEmail();
        phone = patient.getPhone();
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
