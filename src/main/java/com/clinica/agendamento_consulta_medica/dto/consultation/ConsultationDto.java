package com.clinica.agendamento_consulta_medica.dto.consultation;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Setter
@Getter
public class ConsultationDto {

    private Long Id;
    private Instant moment;
    private LocalDate date;
    private LocalTime starTime;
    private Integer duration;
    private Long patient;
    private Long doctor;
    private StatusConsultation statusConsultation;

    public ConsultationDto(Consultation consultation) {
        Id = consultation.getId();
        moment = consultation.getMoment();
        date = consultation.getDate();
        starTime = consultation.getStarTime();
        duration = consultation.getDuration();
        patient = consultation.getPatient().getPatientId();
        statusConsultation = consultation.getStatusConsultation();
        doctor = consultation.getDoctor().getDoctorId();
    }

}
