package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationRequestDTO;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.Patient;

import java.util.List;
import java.util.stream.Collectors;

public class PatientResponseDTO {

    private Long patientId;
    private String name;
    private String email;
    private String phone;
    private List<ConsultationRequestDTO> consultations;

    public PatientResponseDTO(){}

    public PatientResponseDTO(Patient patient) {
        this.patientId = patient.getPatientId();
        this.name = patient.getName();
        this.email = patient.getEmail();
        this.phone = patient.getPhone();
        this.consultations = patient.getConsultations().stream().map(ConsultationRequestDTO::new).collect(Collectors.toList());
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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

    public List<ConsultationRequestDTO> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<ConsultationRequestDTO> consultations) {
        this.consultations = consultations;
    }
}
