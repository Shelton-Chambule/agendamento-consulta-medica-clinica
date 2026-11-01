package com.clinica.agendamento_consulta_medica.mapper;

import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationResponse;
import com.clinica.agendamento_consulta_medica.entity.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ConsultationMapper {

    public ConsultationResponse toResponse(Consultation consultation) {
        return new ConsultationResponse(consultation);
    }

    public Page<ConsultationResponse> toResponsePage(Page<Consultation> consultations) {
        return consultations.map(this::toResponse);
    }
}