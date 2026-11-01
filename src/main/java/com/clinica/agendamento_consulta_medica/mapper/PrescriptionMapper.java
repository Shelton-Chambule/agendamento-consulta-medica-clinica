package com.clinica.agendamento_consulta_medica.mapper;
import com.clinica.agendamento_consulta_medica.dto.prescrition.PrescriptionResponse;
import com.clinica.agendamento_consulta_medica.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    public PrescriptionResponse toResponse(Prescription prescription) {
        return new PrescriptionResponse(prescription);
    }

    public Page<PrescriptionResponse> toResponsePage(Page<Prescription> prescriptions) {
        return prescriptions.map(this::toResponse);
    }
}