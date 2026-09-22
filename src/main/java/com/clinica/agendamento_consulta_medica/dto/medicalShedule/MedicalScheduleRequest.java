package com.clinica.agendamento_consulta_medica.dto.medicalShedule;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MedicalScheduleRequest {

    @JsonFormat(pattern = "H:mm:ss")
    private LocalTime starTime;
    @JsonFormat(pattern = "H:mm:ss")
    private LocalTime endTime;
    @JsonFormat(pattern = "H:mm:ss")
    private LocalTime breakTimes;

    private List<String> daysOfWeek;

    @NotNull
    private Long doctorId;

    public MedicalScheduleRequest(MedicalSchedule medicalSchedule) {
        starTime = medicalSchedule.getStarTime();
        endTime = medicalSchedule.getEndTime();
        doctorId = medicalSchedule.getDoctor().getDoctorId();
        breakTimes = medicalSchedule.getBreakTimes();
        daysOfWeek = medicalSchedule.getDaysOfWeek();
    }
}
