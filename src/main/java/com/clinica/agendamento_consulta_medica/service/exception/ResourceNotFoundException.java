package com.clinica.agendamento_consulta_medica.service.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id) {
        super("Resouce not found: "+ id);
    }
}
