package com.clinica.agendamento_consulta_medica.mapper;
import com.clinica.agendamento_consulta_medica.dto.history.HistoryPatientResponse;
import com.clinica.agendamento_consulta_medica.entity.HistoryPatient;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class HistoryPatientMapper {

    public HistoryPatientResponse toResponse(HistoryPatient historyPatient) {
        return new HistoryPatientResponse(historyPatient);
    }

    public Page<HistoryPatientResponse> toResponsePage(Page<HistoryPatient> historyPatients) {
        return historyPatients.map(this::toResponse);
    }
}