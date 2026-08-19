package com.clinica.agendamento_consulta_medica.dto.history;
import com.clinica.agendamento_consulta_medica.entities.HistoryPatient;
import com.clinica.agendamento_consulta_medica.entities.enums.StatusConsultation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Setter
@Getter
public class HistoryPatientDto {

    private Long id;
    private Long idPatient;
    private String namePatient;
    private LocalTime statTime;
    private LocalDate date;
    private Integer duration;
    private StatusConsultation statusConsultation;


    public HistoryPatientDto(HistoryPatient historyPatient) {
        id = historyPatient.getId();
        idPatient =  historyPatient.getConsultations().getPatient().getPatientId();
        namePatient = historyPatient.getConsultations().getPatient().getName();
        statTime = historyPatient.getConsultations().getStarTime();
        duration = historyPatient.getConsultations().getDuration();
        date = historyPatient.getConsultations().getDate();
        statusConsultation  = historyPatient.getStatusConsultation();
    }
}
