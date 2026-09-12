package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
public class SpecialtyResponseDTO {

    private Long id;
    private String name;
    private Double price;

    public SpecialtyResponseDTO(){}

    public SpecialtyResponseDTO(Specialty specialty){
        this.id = specialty.getId();
        this.name = specialty.getName();
        this.price = specialty.getPrice();
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
