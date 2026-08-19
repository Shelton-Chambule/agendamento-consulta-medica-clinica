package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.dto.consultation.ConsultationDto;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@Getter
public class PatientDto {

    private Long patientId;
    private String name;
    private String email;
    private String phone;
    private List<ConsultationDto> consultations;

    public PatientDto(Patient patient) {
        patientId = patient.getPatientId();
        name = patient.getName();
        email = patient.getEmail();
        phone = patient.getPhone();
        consultations = patient.getConsultations().stream().map(ConsultationDto::new).collect(Collectors.toList());
    }
}
