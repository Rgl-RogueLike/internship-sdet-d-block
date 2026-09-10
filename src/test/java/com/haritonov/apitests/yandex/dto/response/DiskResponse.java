package com.haritonov.apitests.yandex.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO представляющий успешный ответ от эндпоинта /v1/disk/.
 * Содержит основную информацию о диске и пользователе.
 */
@Data
public class DiskResponse {
    private User user;

    /**
     * Вложенный DTO для представления пользователя.
     */
    @Data
    public static class User {
        private String login;

        @JsonProperty("display_name")
        private String displayName;
    }
}
