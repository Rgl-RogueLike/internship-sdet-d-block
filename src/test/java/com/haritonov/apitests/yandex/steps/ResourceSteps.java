package com.haritonov.apitests.yandex.steps;

import com.haritonov.apitests.yandex.dto.response.LinkResponse;
import com.haritonov.apitests.yandex.endpoints.ApiConfig;
import com.haritonov.apitests.yandex.endpoints.Endpoints;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

/**
 * Шаги для взаимодействия с ресурсами
 */
public final class ResourceSteps {

    private ResourceSteps() { }

    /**
     * Шаг: Создание папки по указанному пути.
     *
     * @param path Путь на диске
     * @return DTO LinkResponse со ссылкой на созданный ресурс
     */
    public static LinkResponse createFolder(String path) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path" , path)
                .when()
                .put(Endpoints.RESOURCES)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(LinkResponse.class);
    }

    /**
     * Шаг: Безопасное удаление папки.
     *
     * @param path Путь на диске
     */
    public static void safeDeleteFolder(String path) {
        given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .when()
                .delete(Endpoints.RESOURCES)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Очистка корзины.
     */
    public static void clearTrash() {
        given()
                .spec(ApiConfig.getBaseSpec())
                .when()
                .delete(Endpoints.TRASH_RESOURCES);
    }

    /**
     * Шаг: Попытка создания папки без проверки статус-кода.
     * Используется для негативных тестов, где ожидается ошибка (4хх).
     *
     * @param path Путь на диске
     * @return Ответ от сервера
     */
    public static Response attemptToCreateFolder(String path) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .when()
                .put(Endpoints.RESOURCES)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Попытка создания папки без передачи параметра path.
     *
     * @return Ответ сервера
     */
    public static Response attemptToCreateFolderWithoutPathParam() {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when()
                .put(Endpoints.RESOURCES)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Удаление папки по указанному пути (с проверкой статус-кода 204).
     *
     * @param path Путь на диске
     */
    public static void deleteFolder(String path) {
        given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .when()
                .delete(Endpoints.RESOURCES)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    /**
     * Шаг: Получение информации о ресурсе без проверки статус-кода.
     * Используется для проверки существования папки.
     *
     * @param path Путь на диске
     * @return Ответ сервера
     */
    public static Response getFolderInfoResponse(String path) {
       return given()
               .spec(ApiConfig.getBaseSpec())
               .queryParam("path", path)
               .when()
               .get(Endpoints.RESOURCES)
               .then()
               .extract()
               .response();
    }
}
