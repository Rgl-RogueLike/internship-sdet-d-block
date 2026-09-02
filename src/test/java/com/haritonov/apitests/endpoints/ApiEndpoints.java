package com.haritonov.apitests.endpoints;

/**
 * Класс-хранилище констант эндпоинтов WordPress REST API.
 */
public final class ApiEndpoints {

    private ApiEndpoints() {

    }

    /**
     * Эндпоинт для получения списка постов или создания нового поста.
     */
    public static final String POSTS = "/posts";

    /**
     * Эндпоинт для получения, обновления или удаления конкретного поста по его ID.
     * Параметр {id} заменяется на фактический идентификатор во время выполнения запроса.
     */
    public static final String POST_BY_ID = "/posts/{id}";
}
