package com.clinica.agendamento_consulta_medica.exception;

public class ScheduleConflictException extends RuntimeException {


    public ScheduleConflictException(String message) {
        super(" The doctor already has an appointment scheduled for that time.");
    }
}
