package com.clinica.agendamento_consulta_medica.exception;
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
