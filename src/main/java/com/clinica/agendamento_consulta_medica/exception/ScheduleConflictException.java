package com.clinica.agendamento_consulta_medica.exception;

public class ScheduleConflictException extends RuntimeException {


    public ScheduleConflictException(String message) {
        super(message);
    }
}
