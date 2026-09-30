package com.clinica.agendamento_consulta_medica.dto.medicalShedule;
import com.clinica.agendamento_consulta_medica.entity.MedicalSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @JsonProperty(required = true)
    private LocalTime starTime;

    @JsonProperty(required = true)
    @JsonFormat(pattern = "H:mm:ss")
    @NotNull
    private LocalTime endTime;

    @JsonProperty(required = true)
    @JsonFormat(pattern = "H:mm:ss")
    @NotNull
    private LocalTime breakTimes;

    @JsonProperty(required = true)
    @NotNull
    private List<String> daysOfWeek;

    @JsonProperty(required = true)
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
