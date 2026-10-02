package com.clinica.agendamento_consulta_medica.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id) {
        super("Resource with id " + id + " was not found.");
    }
}
