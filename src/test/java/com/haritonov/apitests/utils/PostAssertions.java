package com.haritonov.apitests.utils;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.db.DatabaseManager;
import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;

/**
 * Класс кастомных проверок (Assertions) для сущности Post.
 * <p>
 * Содержит комплексные проверки, которые валидируют одновременно ответ API (JSON, статус-коды)
 * и фактическое состояние записи в базе данных. Реализован как утилитный класс.
 */
public final class PostAssertions {

    private PostAssertions() {
    }

    /**
     * Проверка успешного создания поста.
     * Сравнивает отправленные данные с ответом API и записью в БД.
     *
     * @param request  отправленный DTO запроса
     * @param response ответ API
     */
    public static void assertPostCreatedSuccessfully(PostRequest request, PostResponse response) {
        Assert.assertTrue(response.getId() > 0, "ID поста должен быть больше 0");
        Assert.assertEquals(response.getStatus(), request.getStatus(),
                "Статус в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getTitle().getRaw(), request.getTitle(),
                "Заголовок в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getContent().getRaw(), request.getContent(),
                "Контент в ответе API должен совпадать с отправленным");

        String dbStatus = DatabaseManager.getPostStatusById(response.getId());
        String dbTitle = DatabaseManager.getPostTitleById(response.getId());
        String dbContent = DatabaseManager.getPostContentById(response.getId());

        Assert.assertEquals(dbStatus, request.getStatus(),
                "Статус в БД должен совпадать с отправленным");
        Assert.assertEquals(dbTitle, request.getTitle(),
                "Заголовок должен совпадать с отправленным");
        // Используем contains, так как WordPress оборачивает контент в HTML-теги (<p>)
        Assert.assertTrue(dbContent.contains(request.getContent()),
                "Контент в БД должен содержать отправленный текст");
    }

    /**
     * Проверка успешного обновления поста.
     *
     * @param updateRequest отправленный DTO с новыми данными
     * @param response      ответ API
     */
    public static void assertPostUpdatedSuccessfully(PostRequest updateRequest, PostResponse response) {
        Assert.assertEquals(response.getStatus(), updateRequest.getStatus(),
                "Статус в ответе API должен совпадать с обновленным");
        Assert.assertEquals(response.getTitle().getRaw(), updateRequest.getTitle(),
                "Заголовок в ответе API должен совпадать с обновленным");

        String dbStatus = DatabaseManager.getPostStatusById(response.getId());
        String dbTitle = DatabaseManager.getPostTitleById(response.getId());

        Assert.assertEquals(dbStatus, updateRequest.getStatus(),
                "Статус в БД должен совпадать с обновленным");
        Assert.assertEquals(dbTitle, updateRequest.getTitle(),
                "Заголовок в БД должен совпадать с обновленным");
    }

    /**
     * Проверка жесткого удаления поста (force=true).
     *
     * @param deleteResponse ответ API
     * @param expectedId     ID удаляемого поста
     */
    public static void assertPostDeletedSuccessfully(Response deleteResponse, int expectedId) {
        Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.SC_OK,
                "Статус код удаления должен быть 200");
        Assert.assertTrue(deleteResponse.jsonPath().getBoolean("deleted"),
                "Поле 'deleted' в ответе должно быть true");
        Assert.assertEquals(deleteResponse.jsonPath().getInt("previous.id"), expectedId,
                "Поле 'previous.id' должно совпадать с ID удаленного поста");

        boolean isPostExists = DatabaseManager.isPostExists(expectedId);
        Assert.assertFalse(isPostExists,
                "Пост не должен существовать в БД после удаления с force=true");
    }

    /**
     * Проверка создания поста со значениями по умолчанию.
     *
     * @param response      ответ API
     * @param expectedTitle заголовок, который был отправлен
     */
    public static void assertPostCreatedWithDefaultValues(PostResponse response, String expectedTitle) {
        Assert.assertTrue(response.getId() > 0,
                "ID поста должен быть больше 0");
        Assert.assertEquals(response.getStatus(), ConfigManager.getTestData().statusDraft(),
                "Статус по умолчанию должен быть 'draft'");
        Assert.assertEquals(response.getTitle().getRaw(), expectedTitle,
                "Заголовок в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getContent().getRaw(), "",
                "Контент по умолчанию должен быть пустой строкой");

        String dbStatus = DatabaseManager.getPostStatusById(response.getId());
        String dbTitle = DatabaseManager.getPostTitleById(response.getId());
        String dbContent = DatabaseManager.getPostContentById(response.getId());

        Assert.assertEquals(dbStatus, ConfigManager.getTestData().statusDraft(),
                "Статус в БД по умолчанию должен быть 'draft'");
        Assert.assertEquals(dbTitle, expectedTitle,
                "Заголовок в БД должен совпадать с отправленным");
        Assert.assertTrue(dbContent == null || dbContent.isEmpty(),
                "Контент в БД должен быть пустым");
    }

    /**
     * Проверка безопасного удаления (перемещения в корзину).
     *
     * @param deleteResponse ответ API
     * @param expectedId     ID удаляемого поста
     */
    public static void assertPostMovedToTrashSuccessfully(Response deleteResponse, int expectedId) {
        Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.SC_OK,
                "Статус код удаления должен быть 200");
        Assert.assertEquals(deleteResponse.jsonPath().getInt("id"), expectedId,
                "ID в ответе должен совпадать с удаляемым постом");
        Assert.assertEquals(deleteResponse.jsonPath().getString("status"),
                ConfigManager.getTestData().statusTrash(),
                "Статус в ответе API должен быть 'trash'");

        boolean isPostExists = DatabaseManager.isPostExists(expectedId);
        Assert.assertTrue(isPostExists, "Пост должен физически оставаться в БД после перемещения в корзину");

        String dbStatus = DatabaseManager.getPostStatusById(expectedId);
        Assert.assertEquals(dbStatus, ConfigManager.getTestData().statusTrash(), "Статус поста в БД должен быть изменен на 'trash'");
    }

    /**
     * Проверка неуспешного создания поста из-за невалидного статуса.
     *
     * @param response      ответ API
     * @param expectedTitle заголовок, который пытались отправить
     */
    public static void assertPostNotCreatedWithInvalidStatus(Response response, String expectedTitle) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorInvalidParam(),
                "Код ошибки должен быть rest_invalid_param");

        int postCount = DatabaseManager.getPostCountByTitle(expectedTitle);
        Assert.assertEquals(postCount, 0,
                "Пост с невалидным статусом не должен создаваться в БД");
    }

    /**
     * Проверка неуспешной операции (обновления/удаления) из-за невалидного ID.
     *
     * @param response  ответ API
     * @param invalidId несуществующий ID
     */
    public static void assertPostNotFoundWithInvalidId(Response response, int invalidId) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorPostInvalidId(),
                "Код ошибки должен быть rest_post_invalid_id");

        boolean isPostExists = DatabaseManager.isPostExists(invalidId);
        Assert.assertFalse(isPostExists, "Поста с невалидным ID не должно существовать в БД");
    }

    /**
     * Проверка неуспешного обновления поста из-за невалидного статуса.
     * Проверяет, что данные в БД не были повреждены (остался прежний статус).
     *
     * @param response      ответ API
     * @param postId        ID поста
     * @param initialStatus изначальный статус поста до попытки обновления
     */
    public static void assertPostNotUpdatedWithInvalidStatus(Response response, int postId, String initialStatus) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorInvalidParam(),
                "Код ошибки должен быть rest_invalid_param");

        String dbStatus = DatabaseManager.getPostStatusById(postId);
        Assert.assertEquals(dbStatus, initialStatus,
                "Статус поста в БД не должен измениться после неудачного обновления");
    }
}