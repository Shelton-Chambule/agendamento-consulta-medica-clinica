package com.clinica.agendamento_consulta_medica.exception;
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(" You don,t  authorization to delete this patient");
    }
}
