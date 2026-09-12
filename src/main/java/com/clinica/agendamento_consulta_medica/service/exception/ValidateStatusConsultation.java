package com.clinica.agendamento_consulta_medica.service.exception;
public class ValidateStatusConsultation extends RuntimeException {
    public ValidateStatusConsultation(String message) {
        super("Error! the prescription cannot be processed because the query status is not completed!");
    }
}
