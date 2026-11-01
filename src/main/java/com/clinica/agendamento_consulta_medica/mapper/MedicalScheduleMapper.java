package com.clinica.agendamento_consulta_medica.mapper;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalSheduleResponse;
import com.clinica.agendamento_consulta_medica.entity.MedicalSchedule;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MedicalScheduleMapper {

    public MedicalSheduleResponse toResponse(MedicalSchedule medicalSchedule) {
        return new MedicalSheduleResponse(medicalSchedule);
    }

    public Page<MedicalSheduleResponse> toResponsePage(Page<MedicalSchedule> medicalSchedules) {
        return medicalSchedules.map(this::toResponse);
    }
}