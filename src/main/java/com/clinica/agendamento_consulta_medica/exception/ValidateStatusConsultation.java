package com.clinica.agendamento_consulta_medica.exception;
public class ValidateStatusConsultation extends RuntimeException {

    public ValidateStatusConsultation(String message) {
        super("Error! the prescription cannot be processed because the query status is not completed!");
    }
}
