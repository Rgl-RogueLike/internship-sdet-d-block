package com.haritonov.apitests.utils;

import com.haritonov.apitests.db.DatabaseManager;
import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import org.testng.Assert;

public final class PostAssertions {

    private PostAssertions() {}

    public static void assertPostCreateSuccessfully(PostRequest request, PostResponse response) {
        Assert.assertTrue(response.getId() > 0, "ID поста должен быть больше 0");
        Assert.assertEquals(response.getStatus(), request.getStatus(),
                "Статус в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getTitle().getRaw(), request.getTitle(),
                "Заголовок в ответе API должен совпадать с отправленным");

        String dbStatus = DatabaseManager.getPostStatusById(response.getId());
        String dbTitle = DatabaseManager.getPostTitleById(response.getId());

        Assert.assertEquals(dbStatus, request.getStatus(),
                "Статус в БД должен совпадать с отправленным");
        Assert.assertEquals(dbTitle, request.getTitle(),
                "Заголовок должен совпадать с отправленным");
    }
}
