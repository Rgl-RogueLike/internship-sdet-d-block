package com.haritonov.apitests.utils;

import com.haritonov.apitests.db.DatabaseManager;
import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;

public final class PostAssertions {

    private PostAssertions() {}

    public static void assertPostCreateSuccessfully(PostRequest request, PostResponse response) {
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
        Assert.assertTrue(dbContent.contains(request.getContent()),
                "Контент в БД должен содержать отправленный текст");
    }

    public static void assertPostUpdatedSuccessfully(PostRequest updateRequest, PostResponse response) {
        Assert.assertEquals(response.getStatus(), updateRequest.getStatus(),
                "Статус в ответе API должен совпадать с обновленным");
        Assert.assertEquals(response.getTitle().getRaw(), updateRequest.getTitle(),
                "Заголовок в ответе API должен совпадать с обновленным");

        String dbStatus = DatabaseManager.getPostStatusById(response.getId());
        String dbTitle = DatabaseManager.getPostTitleById(response.getId());

        Assert.assertEquals(dbStatus, updateRequest.getStatus(),
                "Статус в БД должен совпадать с обновленным");
        Assert.assertEquals(dbTitle ,updateRequest.getTitle(),
                "Заголовок в БД должен совпадать с обновленным");
    }

    public static void assertPostDeletedSuccessfully(Response deleteResponse, int expectedId) {
        Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.SC_OK,
                "Статус код удаления должен быть 200");
        Assert.assertTrue(deleteResponse.jsonPath().getBoolean("deleted"),
                "Поле 'deleted' в ответе должно быть true");
        Assert.assertEquals(deleteResponse.jsonPath().getInt("previous.id"), expectedId,
                "Поле 'previous.id' должно совпадать с ID удаленного поста");

        boolean idPostExists = DatabaseManager.isPostExists(expectedId);
        Assert.assertFalse(idPostExists,
                "пост не должен существовать в БД после удаления с force=true");
    }

    public static void assertPostCreatedWithDefaultValues(PostResponse response, String expectedTitle) {
        Assert.assertTrue(response.getId() > 0,
                "ID поста должен быть больше 0");
        Assert.assertEquals(response.getStatus(), "draft",
                "Статус по умолчанию должен быть 'draft'");
        Assert.assertEquals(response.getTitle().getRaw(), expectedTitle,
                "Заголовок в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getContent().getRaw(), "",
                "Контент по умолчанию должен быть пустой строкой");

        String dbStatus = DatabaseManager.getPostStatusById(response.getId());
        String dbTitle = DatabaseManager.getPostTitleById(response.getId());
        String dbContent = DatabaseManager.getPostContentById(response.getId());

        Assert.assertEquals(dbStatus, "draft",
                "Статус в БД по умолчанию должен быть 'draft'");
        Assert.assertEquals(dbTitle, expectedTitle,
                "Заголовок в БД должен совпадать с отправленным");
        Assert.assertTrue(dbContent == null || dbContent.isEmpty(),
                "Контент в БД должен быть пустым");
    }
}
