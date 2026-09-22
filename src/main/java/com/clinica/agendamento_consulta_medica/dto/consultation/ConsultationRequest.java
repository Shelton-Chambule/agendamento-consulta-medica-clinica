package com.clinica.agendamento_consulta_medica.dto.consultation;
import com.clinica.agendamento_consulta_medica.entities.Consultation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ConsultationRequest {

    private LocalDateTime moment;

    @JsonFormat(pattern ="yyyy-MM-dd")
    @NotNull(message = "date not can be null")
    @FutureOrPresent(message = "The appointment date cannot be in the past.")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "the starTime not can be null")
    private LocalTime startTime;

    @NotNull(message = "the duration not can be null")
    private Duration duration;

    @NotNull(message = "the id patient not can be null")
    @Positive(message = "The value not can negative")
    private Long patient;

    @Positive(message = "The value not can negative")
    @NotNull(message = "the id doctor not can be null")
    private Long doctor;

    public ConsultationRequest(Consultation consultation) {
        moment = consultation.getMoment();
        date = consultation.getDate();
        startTime = consultation.getStartTime();
        duration = consultation.getDuration();
        patient = consultation.getPatient().getPatientId();
        doctor = consultation.getDoctor().getDoctorId();
    }
}
