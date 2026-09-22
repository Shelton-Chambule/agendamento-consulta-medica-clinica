package com.clinica.agendamento_consulta_medica.dto.doctor;
import com.clinica.agendamento_consulta_medica.dto.medicalShedule.MedicalScheduleRequest;
import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
@Setter
@Getter
@NoArgsConstructor
public class DoctorResponse {

    private Long id;
    private String name;
    private String login;
    private String phone;
    private List<String> specialtyDto;
    private List<MedicalScheduleRequest> medicalSchedule;

    public DoctorResponse(Doctor doctor) {
        id = doctor.getDoctorId();
        name = doctor.getName();
        login = doctor.getAccount().getLogin();
        phone = doctor.getPhone();
        specialtyDto = doctor.getSpecialties().stream().map(Specialty::getName).toList();
        medicalSchedule = doctor.getMedicalSchedules().stream().map(MedicalScheduleRequest::new).collect(Collectors.toList());
    }
}
