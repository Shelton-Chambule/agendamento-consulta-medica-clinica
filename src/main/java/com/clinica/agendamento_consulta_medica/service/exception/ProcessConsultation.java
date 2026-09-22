package com.clinica.agendamento_consulta_medica.service.exception;

public class ProcessConsultation extends RuntimeException {

    public ProcessConsultation(String message) {
        super("Error while process consultation");
    }
}
