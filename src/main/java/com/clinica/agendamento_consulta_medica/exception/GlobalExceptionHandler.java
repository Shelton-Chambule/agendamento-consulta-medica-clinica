package com.clinica.agendamento_consulta_medica.exception;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException r, HttpServletRequest request) {
        log.warn("Resource not found at {}: {}", request.getRequestURI(), r.getMessage());
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, r.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBase(DataBaseException data, HttpServletRequest request){
        log.error("Database constraint violation at {}: {}", request.getRequestURI(), data.getMessage(), data);
        String error = "Database constraint violation";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, data.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<StandardError> scheduleConflict(ScheduleConflictException data, HttpServletRequest request){
        log.warn("Schedule conflict at {}: {}", request.getRequestURI(), data.getMessage());
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
        log.warn("Validation failed at {}: {}", request.getRequestURI(), message);
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, message,request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(UniqueLoginException.class)
    public ResponseEntity<StandardError> uniqueEmail(UniqueLoginException uniqueEmail, HttpServletRequest request){
        log.warn("Duplicate login at {}: {}", request.getRequestURI(), uniqueEmail.getMessage());
        String error = "Duplicate login";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, uniqueEmail.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ValidateStatusConsultation.class)
    public ResponseEntity<StandardError> validateStatus(ValidateStatusConsultation validateStatusConsultation, HttpServletRequest request){
        log.warn("Invalid consultation status at {}: {}", request.getRequestURI(), validateStatusConsultation.getMessage());
        String error = "Invalid consultation status";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(), status.value(), error, validateStatusConsultation.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDenied(AccessDeniedException access, HttpServletRequest request){
        log.warn("Access denied at {}: {}", request.getRequestURI(), access.getMessage());
        String error = "Access denied";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(Instant.now(),status.value(),error, access.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ProcessConsultation.class)
    public ResponseEntity<StandardError> processConsultation(ProcessConsultation exception, HttpServletRequest request) {
        log.warn("Invalid consultation state at {}: {}", request.getRequestURI(), exception.getMessage());
        return response(HttpStatus.CONFLICT, "Invalid consultation state", exception.getMessage(), request);
    }

    @ExceptionHandler(LoginNotFound.class)
    public ResponseEntity<StandardError> loginNotFound(LoginNotFound exception, HttpServletRequest request) {
        log.warn("Login not found at {}: {}", request.getRequestURI(), exception.getMessage());
        return response(HttpStatus.NOT_FOUND, "Login not found", exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        log.warn("Invalid request at {}: {}", request.getRequestURI(), exception.getMessage());
        return response(HttpStatus.BAD_REQUEST, "Invalid request", exception.getMessage(), request);
    }

    private ResponseEntity<StandardError> response(HttpStatus status, String error, String message,
                                                   HttpServletRequest request) {
        return ResponseEntity.status(status).body(new StandardError(
                Instant.now(), status.value(), error, message, request.getRequestURI()));
    }
}