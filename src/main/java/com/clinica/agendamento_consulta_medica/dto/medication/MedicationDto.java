package com.clinica.agendamento_consulta_medica.dto.medication;
import com.clinica.agendamento_consulta_medica.entities.Medications;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MedicationDto {

    private Long id;
    private String name;

    public MedicationDto(Medications medications) {
        this.id = medications.getId();
        this.name = medications.getName();
    }

}
