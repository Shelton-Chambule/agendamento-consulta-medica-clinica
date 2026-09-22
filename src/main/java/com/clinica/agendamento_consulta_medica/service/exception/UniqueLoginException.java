package com.clinica.agendamento_consulta_medica.service.exception;

public class UniqueLoginException extends RuntimeException {
    public UniqueLoginException(String message) {
        super("This email already register!");
    }
}
