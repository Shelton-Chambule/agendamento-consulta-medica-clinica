package com.clinica.agendamento_consulta_medica.mapper;
import com.clinica.agendamento_consulta_medica.dto.patient.PatientResponse;
import com.clinica.agendamento_consulta_medica.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient);
    }

    public Page<PatientResponse> toResponsePage(Page<Patient> patients) {
        return patients.map(this::toResponse);
    }
}