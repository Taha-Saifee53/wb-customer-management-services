package com.wb.assignment.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnifiedResponse<T> implements Serializable {

    private T response;
    private int responseCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private List<String> validationErrors;

    /* ================= METHODS ================= */

    public static <T> UnifiedResponse<T> success(T data, HttpStatus status) {
        return UnifiedResponse.<T>builder()
                .response(data)
                .responseCode(status.value())
                .httpStatus(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> UnifiedResponse<T> failure(
            String errorMessage,
            HttpStatus status,
            List<String> validationErrors) {

        return UnifiedResponse.<T>builder()
                .errorMessage(errorMessage)
                .responseCode(status.value())
                .httpStatus(status)
                .validationErrors(validationErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

