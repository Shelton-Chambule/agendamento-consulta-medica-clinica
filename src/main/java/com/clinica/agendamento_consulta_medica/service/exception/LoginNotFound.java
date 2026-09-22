package com.clinica.agendamento_consulta_medica.service.exception;

public class LoginNotFound extends RuntimeException {
    public LoginNotFound(String message) {
        super("login not found");
    }
}
