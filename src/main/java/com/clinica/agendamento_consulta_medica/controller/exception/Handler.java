package com.clinica.agendamento_consulta_medica.controller.exception;
import com.clinica.agendamento_consulta_medica.service.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException r, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, r.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBase(DataBaseException data, HttpServletRequest request){
        String error = "Data base exception";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, data.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<StandardError> scheduleConflict(ScheduleConflictException data, HttpServletRequest request){
        String error = "hours failed";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, data.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationException(MethodArgumentNotValidException data, HttpServletRequest request){
        String error = "Error of validation";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, data.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(UniqueEmailException.class)
    public ResponseEntity<StandardError> uniqueEmail(UniqueEmailException uniqueEmail, HttpServletRequest request){
        String error = "Error of validation";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, uniqueEmail.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ValidateStatusConsultation.class)
    public ResponseEntity<StandardError> validateStatus(ValidateStatusConsultation validateStatusConsultation, HttpServletRequest request){
        String error = "Error of validation";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, validateStatusConsultation.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }


}
