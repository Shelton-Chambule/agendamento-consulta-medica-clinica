package com.clinica.agendamento_consulta_medica.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException r, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, r.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBase(DataBaseException data, HttpServletRequest request){
        String error = "Database constraint violation";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, data.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<StandardError> scheduleConflict(ScheduleConflictException data, HttpServletRequest request){
        String error = "Schedule conflict";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, data.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationException(MethodArgumentNotValidException data, HttpServletRequest request){
        String error = "Validation failed";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        String message = data.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, message,request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(UniqueLoginException.class)
    public ResponseEntity<StandardError> uniqueEmail(UniqueLoginException uniqueEmail, HttpServletRequest request){
        String error = "Duplicate login";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, uniqueEmail.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ValidateStatusConsultation.class)
    public ResponseEntity<StandardError> validateStatus(ValidateStatusConsultation validateStatusConsultation, HttpServletRequest request){
        String error = "Invalid consultation status";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, validateStatusConsultation.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDenied(AccessDeniedException access, HttpServletRequest request){
        String error = "Access denied";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(Instant.now(),status.value(),error, access.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ProcessConsultation.class)
    public ResponseEntity<StandardError> processConsultation(ProcessConsultation exception, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Invalid consultation state", exception.getMessage(), request);
    }

    @ExceptionHandler(LoginNotFound.class)
    public ResponseEntity<StandardError> loginNotFound(LoginNotFound exception, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Login not found", exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Invalid request", exception.getMessage(), request);
    }

    private ResponseEntity<StandardError> response(HttpStatus status, String error, String message,
                                                   HttpServletRequest request) {
        return ResponseEntity.status(status).body(new StandardError(
                Instant.now(), status.value(), error, message, request.getRequestURI()));
    }
}
