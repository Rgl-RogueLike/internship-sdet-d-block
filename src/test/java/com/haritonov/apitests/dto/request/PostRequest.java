package com.haritonov.apitests.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * DTO представляющий тело запроса для создания или обновления поста.
 * <p>
 * Используется библиотекой Jackson для автоматической
 * сериализации Java-объекта в JSON-строку при отправке запроса.
 */
@Data
@Builder
public class PostRequest {
    private String title;
    private String content;
    private String status;
}
