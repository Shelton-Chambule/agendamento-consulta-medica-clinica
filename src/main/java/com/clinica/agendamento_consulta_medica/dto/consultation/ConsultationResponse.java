package com.clinica.agendamento_consulta_medica.dto.consultation;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
public class ConsultationResponse {

    private Long Id;
    private LocalDateTime moment;
    private LocalDate date;
    private LocalTime startTime;
    private Duration duration;
    private Long patient;
    private Long doctor;
    private StatusConsultation statusConsultation;


    public ConsultationResponse(Consultation consultation) {
        Id = consultation.getId();
        this.moment  = consultation.getMoment();
        this.date =consultation.getDate() ;
        this.startTime = consultation.getStarTime();
        this.duration = consultation.getDuration();
        this.patient = consultation.getPatient().getPatientId();
        this.doctor = consultation.getDoctor().getDoctorId();
        this.statusConsultation =  consultation.getStatusConsultation();
    }
}
