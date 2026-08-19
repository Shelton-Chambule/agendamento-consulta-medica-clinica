package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SpecialtyDto {

    private Long id;
    private String name;
    private Double price;

    public SpecialtyDto(Specialty specialty) {
        id = specialty.getId();
        name = specialty.getName();
        price = specialty.getPrice();
    }

}
