package com.haritonov.apitests.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.db.PostDao;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.steps.PostApiSteps;
import com.haritonov.apitests.utils.DataGenerator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PostsGetTests extends BaseTest {
    private final List<Integer> dbCreatedPostIds = new ArrayList<>();

    @AfterMethod
    public void cleanUpDbData() {
        dbCreatedPostIds.forEach(PostDao::deletePostDirectly);
        dbCreatedPostIds.clear();
    }

    @Test(description = "TC-010: Получение данных поста по существующему ID")
    public void shouldGetPostWhenIdExists() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        String uniqueContent = DataGenerator.generatePostContent();
        String status = ConfigManager.getTestData().statusPublish();

        int postId = PostDao.createPostDirectly(uniqueTitle, uniqueContent, status);
        dbCreatedPostIds.add(postId);

        Response response = PostApiSteps.getPost(postId);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");
        PostResponse postResponse = response.as(PostResponse.class);
        Assert.assertEquals(postResponse.getId(), postId);
        Assert.assertEquals(postResponse.getTitle().getRaw(), uniqueTitle);
        Assert.assertEquals(postResponse.getStatus(), status);
    }
}