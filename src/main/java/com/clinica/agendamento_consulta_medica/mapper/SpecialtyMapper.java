package com.clinica.agendamento_consulta_medica.mapper;
import com.clinica.agendamento_consulta_medica.dto.specialty.SpecialtyResponse;
import com.clinica.agendamento_consulta_medica.entity.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyMapper {

    public SpecialtyResponse toResponse(Specialty specialty) {
        return new SpecialtyResponse(specialty);
    }

    public Page<SpecialtyResponse> toResponsePage(Page<Specialty> specialties) {
        return specialties.map(this::toResponse);
    }
}