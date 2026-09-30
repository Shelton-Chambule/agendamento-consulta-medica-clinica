package com.clinica.agendamento_consulta_medica.exception;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class StandardError {

    private Instant timeStamp;
    private  Integer status;
    private String error;
    private String message;
    private String path;

    public StandardError(Instant timeStamp, Integer status, String error, String message, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
