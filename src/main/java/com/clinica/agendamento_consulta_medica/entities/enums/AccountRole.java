package com.clinica.agendamento_consulta_medica.entities.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole {

    ADMIN("admin"),
    DOCTOR("doctor"),
    PATIENT("patient");

    private String role;

}
