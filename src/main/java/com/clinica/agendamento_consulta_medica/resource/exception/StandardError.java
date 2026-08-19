package com.clinica.agendamento_consulta_medica.resource.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StandardError {

    private Instant timeStamp;
    private  Integer status;
    private String error;
    private String message;
    private String path;
}
