package com.clinica.agendamento_consulta_medica.dto.medicalShedule;
import com.clinica.agendamento_consulta_medica.entities.MedicalSchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@NoArgsConstructor
@Setter
@Getter
public class MedicalScheduleDto {

    private Long id;
    private LocalTime starTime;
    private LocalTime endTime;
    private LocalTime breakTimes;
    private Long doctorId;
    private String doctorName;


    public MedicalScheduleDto(MedicalSchedule medicalSchedule) {
        id = medicalSchedule.getId();
        starTime = medicalSchedule.getStarTime();
        endTime = medicalSchedule.getEndTime();
        doctorId = medicalSchedule.getDoctor().getDoctorId();
        doctorName = medicalSchedule.getDoctor().getName();
        breakTimes = medicalSchedule.getBreakTimes();
    }
}
