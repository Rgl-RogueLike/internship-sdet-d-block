package com.haritonov.apitests.steps;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.endpoints.ApiConfig;
import com.haritonov.apitests.endpoints.ApiEndpoints;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

/**
 * Класс шагов для взаимодействия с сущностью Post через WordPress REST API.
 * <p>
 * Инкапсулирует в себе логику отправки HTTP-запросов с использованием спецификации из {@link ApiConfig}.
 * Методы, ожидающие успешный результат, возвращают десериализованный DTO {@link PostResponse}.
 * Методы для негативных тестов (с префиксом {@code attempt}) возвращают сырой {@link Response} без проверки статус-кода.
 */
public final class PostApiSteps {

    private PostApiSteps() {
    }

    /**
     * Шаг: Создание поста.
     * <p>
     * Отправляет POST запрос на создание поста и проверяет, что сервер вернул статус 201 Created.
     *
     * @param postRequest DTO с данными для создания
     * @return DTO с ответом сервера
     */
    public static PostResponse createPost(PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POSTS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(PostResponse.class);
    }

    /**
     * Шаг: Удаление поста.
     * <p>
     * Отправляет DELETE запрос. Возвращает сырой ответ, так как ожидаемый статус-код
     * зависит от параметра {@code force} (200 при успехе, 404 если пост не найден).
     *
     * @param id    ID удаляемого поста
     * @param force true — удалить сразу физически, false — переместить в корзину
     * @return объект {@link Response}
     */
    public static Response deletePost(int id, boolean force) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("force", force)
                .when()
                .delete(ApiEndpoints.POST_BY_ID, id)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Редактирование поста.
     * <p>
     * Отправляет POST запрос на обновление поста и проверяет, что сервер вернул статус 200 OK.
     *
     * @param id          ID обновляемого поста
     * @param postRequest DTO с новыми данными
     * @return DTO с ответом сервера
     */
    public static PostResponse updatePost(int id, PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POST_BY_ID, id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(PostResponse.class);
    }

    /**
     * Шаг: Попытка создания поста без проверки статус-кода.
     * <p>
     * Используется для негативных тестов, где ожидается ошибка (4xx), и проверка статус-кода
     * переносится в класс Assertions.
     *
     * @param postRequest DTO с данными
     * @return объект {@link Response}
     */
    public static Response attemptToCreatePost(PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POSTS)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Попытка обновления поста без проверки статус-кода.
     * <p>
     * Используется для негативных тестов.
     *
     * @param id          ID поста
     * @param postRequest DTO с данными
     * @return объект {@link Response}
     */
    public static Response attemptToUpdatePost(int id, PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POST_BY_ID, id)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Получение данных поста по ID (с контекстом edit для доступа к raw полям).
     *
     * @param id ID поста
     * @return Ответ сервера
     */
    public static Response getPost(int id) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("context", ConfigManager.getTestData().contextEdit())
                .when()
                .get(ApiEndpoints.POST_BY_ID, id)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Поиск постов по переданному тексту.
     * <p>
     * Автоматически добавляет параметр {@code context=edit} для получения доступа к raw полям.
     *
     * @param searchText Текст для поиска (заголовок или контент)
     * @return объект {@link Response}
     */
    public static Response searchPosts(String searchText) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("search", searchText)
                .queryParam("context", ConfigManager.getTestData().contextEdit())
                .when()
                .get(ApiEndpoints.POSTS)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Фильтрация постов по статусу и поисковому тексту.
     *
     * @param status     Статус поста
     * @param searchText Текст для поиска (заголовок или контент)
     * @return Ответ сервера
     */
    public static Response getPostsByStatusAndSearch(String status, String searchText) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("status", status)
                .queryParam("search", searchText)
                .queryParam("context", ConfigManager.getTestData().contextEdit())
                .when()
                .get(ApiEndpoints.POSTS)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Получение списка постов с фильтрацией по статусу.
     *
     * @param status Статус поста
     * @return Ответ сервера
     */
    public static Response getPostsByStatus(String status) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("status", status)
                .when()
                .get(ApiEndpoints.POSTS)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Получение данных поста по строковому значению.
     * @param stringId Строковое значение ID
     * @return Ответ сервера
     */
    public static Response getPostByStringId(String stringId) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when()
                .get(ApiEndpoints.POST_BY_ID, stringId)
                .then()
                .extract()
                .response();
    }
}
