package com.haritonov.apitests.yandex.dto.response;

import lombok.Data;

/**
 * DTO представляющий ответ с ошибкой.
 * Содержит информацию о причине возникновения ошибки.
 */
@Data
public class ErrorResponse {
    private String message;
    private String description;
    private String error;
}
