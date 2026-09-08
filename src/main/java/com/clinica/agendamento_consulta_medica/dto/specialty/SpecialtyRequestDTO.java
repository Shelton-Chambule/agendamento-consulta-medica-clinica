package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
public class SpecialtyRequestDTO {

    private Long id;
    private String name;
    private Double price;

    public SpecialtyRequestDTO(Specialty specialty) {
        id = specialty.getId();
        name = specialty.getName();
        price = specialty.getPrice();
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
