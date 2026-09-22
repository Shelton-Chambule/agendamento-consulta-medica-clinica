package com.clinica.agendamento_consulta_medica.dto.medicalShedule;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class MedicalSheduleResponse {

    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;
    private Long doctorId;
    private List<String> daysOfWeek;

    public MedicalSheduleResponse(MedicalSchedule medicalSchedule) {
        this.id = medicalSchedule.getId();
        this.starTime = medicalSchedule.getStarTime();
        this.endTime = medicalSchedule.getEndTime();
        this.breakTimes =  medicalSchedule.getBreakTimes();
        this.doctorId = medicalSchedule.getDoctor().getDoctorId();
    }
}
