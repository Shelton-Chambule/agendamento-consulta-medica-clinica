package com.clinica.agendamento_consulta_medica.dto.doctor;

import com.clinica.agendamento_consulta_medica.entities.Doctor;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class DoctorDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<String> specialtyDto;
    private List<Set<DayOfWeek>> medicalSchedule;

    public DoctorDto(Doctor doctor) {
        id = doctor.getDoctorId();
        name = doctor.getName();
        email = doctor.getEmail();
        phone = doctor.getPhone();
        specialtyDto = doctor.getSpecialties().stream().map(Specialty::getName).toList();
        medicalSchedule = doctor.getMedicalSchedules().stream().map(MedicalSchedule::getDayOfWeek).toList();
    }

}
