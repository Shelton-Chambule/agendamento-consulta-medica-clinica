package com.clinica.agendamento_consulta_medica.dto.medication;
import com.clinica.agendamento_consulta_medica.entities.Medications;
public class MedicationRequestDTO {

    private String name;

    public MedicationRequestDTO(){}

    public MedicationRequestDTO(Medications medications) {
        this.name = medications.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
