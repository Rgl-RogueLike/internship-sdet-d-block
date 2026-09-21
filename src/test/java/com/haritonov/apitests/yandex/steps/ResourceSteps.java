package com.haritonov.apitests.yandex.steps;

import com.haritonov.apitests.yandex.dto.response.LinkResponse;
import com.haritonov.apitests.yandex.dto.response.TrashItem;
import com.haritonov.apitests.yandex.dto.response.TrashResponse;
import com.haritonov.apitests.yandex.endpoints.ApiConfig;
import com.haritonov.apitests.yandex.endpoints.Endpoints;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Optional;

import static io.restassured.RestAssured.given;

/**
 * Шаги для взаимодействия с ресурсами
 */
public final class ResourceSteps {

    private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";

    private ResourceSteps() {
    }

    /**
     * Шаг: Создание папки по указанному пути.
     *
     * @param path Путь на диске
     * @return DTO LinkResponse со ссылкой на созданный ресурс
     */
    public static LinkResponse createFolder(String path) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
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
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                .response();
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

    /**
     * Шаг: Попытка удаления папки без проверки статус-кода.
     * Используется для негативных тестов, где ожидается ошибка
     *
     * @param path Путь на диске
     * @return Ответ от сервера
     */
    public static Response attemptToDeleteFolder(String path) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .when()
                .delete(Endpoints.RESOURCES)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Поиск реального пути (с хэшем) удаленной папки в корзине по её оригинальному пути.
     *
     * @param originPath Оригинальный путь папки
     * @return Путь папки в корзине или null, если не найдено.
     */
    public static String findTrashedFolderPathByOrigin(String originPath) {
        Response response = given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", "/")
                .when()
                .get(Endpoints.TRASH_RESOURCES)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        return Optional.ofNullable(response.as(TrashResponse.class))
                .map(TrashResponse::getEmbedded)
                .map(TrashResponse.Embedded::getItems)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(item -> originPath.equals(item.getOriginPath()))
                .map(TrashItem::getPath)
                .findFirst()
                .orElse(null);
    }

    /**
     * Шаг: Восстановление папки из корзины по ее пути (с хэшем).
     *
     * @param trashPath Путь папки в корзине
     * @return DTO {@link LinkResponse} со ссылкой на восстановленный ресурс
     */
    public static LinkResponse restoreFolderFromTrash(String trashPath) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", trashPath)
                .when()
                .put(Endpoints.TRASH_RESTORE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(LinkResponse.class);
    }

    /**
     * Шаг: Попытка восстановления папки из корзины без проверки статус-кода.
     *
     * @param path Путь ресурса в корзине
     * @return Ответ сервера
     */
    public static Response attemptToRestoreFolderFromTrash(String path) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .when()
                .put(Endpoints.TRASH_RESTORE)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Получение временной ссылки для загрузки файла на Диск.
     *
     * @param path Путь на Диске, куда будет загружен файл
     * @param overwrite Флаг перезаписи существующего файла
     * @return DTO {@link LinkResponse} включает временную ссылку для загрузки
     */
    public static LinkResponse getUploadLink(String path, boolean overwrite) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .queryParam("overwrite", overwrite)
                .when()
                .get(Endpoints.UPLOAD)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(LinkResponse.class);
    }

    /**
     * Шаг: Загрузка локального файла на Яндекс.Диск по временной ссылке.
     * <p>
     * Отправляет PUT запрос с байтами файла. Не использует базовую спецификацию,
     * так как загрузка осуществляется по уникальному временному URL. Ожидается статус 201 Created.
     *
     * @param uploadUrl Временная ссылка полученная из метода {@link #getUploadLink(String, boolean)}
     * @param file Локальный файл {@link File} для загрузки
     */
    public static void uploadFileToDisk(String uploadUrl, File file) {
        given()
                .header("Content-type", OCTET_STREAM_CONTENT_TYPE)
                .body(file)
                .when()
                .put(uploadUrl)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    /**
     * Шаг: Попытка копировать файл без проверки статус-кода.
     * <p>
     * Используется для проверок ка успеха (201 Created), так и конфликта (409 Conflict).
     *
     * @param from Путь откуда копировать
     * @param to Путь куда копировать
     * @return Ответ сервера {@link Response}
     */
    public static Response attemptToCopyFile(String from, String to) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("from", from)
                .queryParam("path", to)
                .when()
                .post(Endpoints.COPY)
                .then()
                .extract()
                .response();
    }

    /**
     * Шаг: Получение временной ссылки для скачивания файла с Диска.
     *
     * @param path Путь к файлу на Диске
     * @return DTO {@link LinkResponse} со ссылкой на скачивание
     */
    public static LinkResponse getDownloadLink(String path) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("path", path)
                .when()
                .get(Endpoints.DOWNLOAD)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(LinkResponse.class);
    }

    /**
     * Шаг: Скачивание файла по временной ссылке и возврат его содержимого в виде строки.
     *
     * @param downloadUrl Временная ссылка из метода {@link #getDownloadLink(String)}
     * @return Строка с содержимым скачанного файла
     */
    public static String downloadFileContent(String downloadUrl) {
        return given()
                .urlEncodingEnabled(false)
                .when()
                .get(downloadUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString();
    }
}
