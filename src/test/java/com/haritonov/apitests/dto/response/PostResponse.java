package com.haritonov.apitests.dto.response;

import lombok.Data;


/**
 * DTO представляющий тело ответа от API при операциях с постами.
 * <p>
 * Содержит основные поля поста, а также вложенные объекты {@link Title} и {@link Content},
 * которые возвращаются WordPress в виде сложной JSON-структуры с полями {@code raw} и {@code rendered}.
 */
@Data
public class PostResponse {
    private int id;
    private String status;
    private String link;
    private Title title;
    private Content content;

    /**
     * Вложенный DTO для представления заголовка поста.
     */
    @Data
    public static class Title {
        private String raw;
        private String rendered;
    }

    /**
     * Вложенный DTO для представления контента поста.
     */
    @Data
    public static class Content {
        private String raw;
        private String rendered;
    }
}
