package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
public class SpecialtyRequestDTO {

    private String name;
    private Double price;

    public SpecialtyRequestDTO(Specialty specialty) {
        name = specialty.getName();
        price = specialty.getPrice();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
