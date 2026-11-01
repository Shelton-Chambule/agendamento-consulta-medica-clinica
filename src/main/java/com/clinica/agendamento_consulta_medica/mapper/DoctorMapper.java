package com.clinica.agendamento_consulta_medica.mapper;
import com.clinica.agendamento_consulta_medica.dto.doctor.DoctorResponse;
import com.clinica.agendamento_consulta_medica.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(doctor);
    }

    public Page<DoctorResponse> toResponsePage(Page<Doctor> doctors) {
        return doctors.map(this::toResponse);
    }
}