package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.entities.Patient;
public class PatientRequestDTO {

    private String name;
    private String email;
    private String phone;

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
