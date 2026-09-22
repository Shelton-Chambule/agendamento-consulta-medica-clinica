package com.clinica.agendamento_consulta_medica.dto.patient;
import com.clinica.agendamento_consulta_medica.entities.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class PatientResponse {

    private Long patientId;
    private String name;
    private String phone;
    private LocalDate dataNascimento;

    public PatientResponse(Patient patient) {
        patientId = patient.getPatientId();
        name = patient.getName();
        phone = patient.getPhone();
        dataNascimento = patient.getDataNascimento();
    }
}
