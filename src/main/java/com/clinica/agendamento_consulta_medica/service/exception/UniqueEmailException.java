package com.clinica.agendamento_consulta_medica.service.exception;

public class UniqueEmailException extends RuntimeException {
    public UniqueEmailException(String message) {
        super("This email already register!");
    }
}
