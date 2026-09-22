package com.clinica.agendamento_consulta_medica.dto.specialty;
import com.clinica.agendamento_consulta_medica.entities.Specialty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SpecialtyResponse {

    private Long id;
    private String name;

    public SpecialtyResponse(Specialty specialty){
        this.id = specialty.getId();
        this.name = specialty.getName();
    }
}
