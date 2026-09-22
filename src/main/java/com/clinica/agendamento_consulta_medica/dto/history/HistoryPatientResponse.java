package com.clinica.agendamento_consulta_medica.dto.history;
import com.clinica.agendamento_consulta_medica.entities.HistoryPatient;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
public class HistoryPatientResponse {

    private Long historyId;
    private Long patientId;
    private String namePatient;
    private LocalTime statTime;
    private LocalDate date;
    private Duration duration;
    private StatusConsultation statusConsultation;

    public HistoryPatientResponse(HistoryPatient historyPatient) {
        historyId = historyPatient.getId();
        patientId =  historyPatient.getConsultation().getPatient().getPatientId();
        namePatient = historyPatient.getConsultation().getPatient().getName();
        statTime = historyPatient.getConsultation().getStarTime();
        duration = historyPatient.getConsultation().getDuration();
        date = historyPatient.getConsultation().getDate();
        statusConsultation  = historyPatient.getStatusConsultation();
    }
}
