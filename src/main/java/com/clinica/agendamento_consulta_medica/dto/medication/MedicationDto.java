package com.clinica.agendamento_consulta_medica.dto.medication;
import com.clinica.agendamento_consulta_medica.entities.Medications;
public class MedicationDto {

    private Long id;
    private String name;

    public MedicationDto(){}

    public MedicationDto(Medications medications) {
        this.id = medications.getId();
        this.name = medications.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
