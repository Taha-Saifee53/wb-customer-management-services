package com.wb.assignment.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ServiceExceptionHandlerTest {

    private ServiceExceptionHandler exceptionHandler;

    @Mock
    private ConstraintViolation<?> violation;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new ServiceExceptionHandler();
    }

    @Test
    void handleBusinessException_shouldReturnBadRequest() {
        var ex = new BusinessException("Business error");
        var response = exceptionHandler.handleBusinessException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assert response.getBody() != null;
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Business error");
    }

    @Test
    void handleEntityNotFound_shouldReturnNotFound() {
        var ex = new EntityNotFoundException("Customer not found");
        var response = exceptionHandler.handleEntityNotFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assert response.getBody() != null;
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Customer not found");
    }

    @Test
    void handleConstraintViolation_shouldReturnBadRequestWithErrors() {
        when(violation.getMessage()).thenReturn("Invalid value");
        var ex = new ConstraintViolationException(Set.of((ConstraintViolation<?>) violation));
        var response = exceptionHandler.handleConstraintViolation(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assert response.getBody() != null;
        assertThat(response.getBody().getValidationErrors()).contains("Invalid value");
    }

    @Test
    void handleValidationErrors_shouldReturnBadRequestWithFieldErrors() {
        var ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("customer", "name", "must not be blank");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        var response = exceptionHandler.handleValidationErrors(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assert response.getBody() != null;
        assertThat(response.getBody().getValidationErrors())
                .containsExactly("name: must not be blank");
    }

    @Test
    void handleGlobalException_shouldReturnInternalServerError() {
        var ex = new RuntimeException("Something went wrong");
        var response = exceptionHandler.handleGlobalException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assert response.getBody() != null;
        assertThat(response.getBody().getErrorMessage()).isEqualTo("Unexpected error occurred");
    }
}
