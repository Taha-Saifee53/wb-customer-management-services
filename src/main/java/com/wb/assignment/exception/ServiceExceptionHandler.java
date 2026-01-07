package com.wb.assignment.exception;

import com.wb.assignment.response.UnifiedResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

import static com.wb.assignment.response.UnifiedResponse.failure;

@RestControllerAdvice
@Slf4j
public class ServiceExceptionHandler {

    /* ================= BUSINESS EXCEPTION ================= */

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleBusinessException(BusinessException ex) {

        log.info("Business Exception Occurred {}", ex.getMessage());
        var error = failure(ex.getMessage(), HttpStatus.BAD_REQUEST, null);

        return ResponseEntity.badRequest().body(error);
    }

    /* ================= CONSTRAINT VIOLATION ================= */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {

        log.info("Constraint Violation Exception Occurred {}", ex.getConstraintViolations());
        var errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        var error = failure(ex.getMessage(), HttpStatus.BAD_REQUEST, errors);

        return ResponseEntity.badRequest().body(error);
    }

    /* ================= NOT FOUND ================= */

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<UnifiedResponse<Object>> handleEntityNotFound(EntityNotFoundException ex) {

        log.info("Entity Not Found Exception Occurred {}", ex.getMessage());
        var error = failure(ex.getMessage(), HttpStatus.NOT_FOUND, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /* ================= GLOBAL EXCEPTION HANDLER ================= */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UnifiedResponse<Object>> handleGlobalException(Exception ex) {

        log.error("Occurred Global Exception {}", ex.getMessage());
        var error = failure("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
