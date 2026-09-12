package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class SpecialtyRequestDTO {

    @NotBlank(message = "Required field")
    private String name;

    @NotNull(message = "Required field")
    private Double price;

    private Long doctorId;

    public SpecialtyRequestDTO(){}

    public SpecialtyRequestDTO(Specialty specialty) {
        name = specialty.getName();
        price = specialty.getPrice();
        doctorId = specialty.getDoctors().getDoctorId();
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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
