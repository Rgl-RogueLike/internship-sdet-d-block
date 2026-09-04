package com.haritonov.apitests.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.db.PostDao;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.steps.PostApiSteps;
import com.haritonov.apitests.utils.PostsDbHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostsGetTests extends BaseTest {

    /**
     * Список для хранения ID постов, созданных напрямую в БД
     */
    private final List<Integer> dbCreatedPostIds = new ArrayList<>();

    /**
     * Очистка тестовых данных после каждого теста.
     * Удаляет все созданные в рамках теста посты, чтобы не засорять БД.
     */
    @AfterMethod
    public void cleanUpDbData() {
        dbCreatedPostIds.forEach(PostDao::deletePostDirectly);
        dbCreatedPostIds.clear();
    }

    @Test(description = "TC-010: Получение данных поста по существующему ID")
    public void shouldGetPostWhenIdExists() {
        PostsDbHelper.DbTestPost testPost = PostsDbHelper.createTestPostInDb(ConfigManager.getTestData().statusPublish());
        dbCreatedPostIds.add(testPost.id());

        Response response = PostApiSteps.getPost(testPost.id());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");
        PostResponse postResponse = response.as(PostResponse.class);
        Assert.assertEquals(postResponse.getId(), testPost.id(),
                "ID поста в ответе должен совпадать с ID из БД");
        Assert.assertEquals(postResponse.getTitle().getRaw(), testPost.title(),
                "Заголовок raw должен совпадать с созданным в БД");
        Assert.assertEquals(postResponse.getStatus(), ConfigManager.getTestData().statusPublish(),
                "Статус поста должен совпадать с созданным в БД");
    }

    @Test(description = "TC-011: Поиск постов по заголовку")
    public void shouldFindPostWhenSearchByTitle() {
        PostsDbHelper.DbTestPost testPost = PostsDbHelper.createTestPostInDb(ConfigManager.getTestData().statusPublish());
        dbCreatedPostIds.add(testPost.id());

        Response response = PostApiSteps.searchPosts(testPost.title());

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");

        PostResponse[] foundPostsArray = response.as(PostResponse[].class);
        List<PostResponse> foundPosts = Arrays.asList(foundPostsArray);

        Assert.assertFalse(foundPosts.isEmpty(),
                "Массив найденных постов не должен быть пустым");
        Assert.assertEquals(foundPosts.getFirst().getId(), testPost.id(),
                "ID первого найденного поста должен совпадать с созданным");
        Assert.assertEquals(foundPosts.getFirst().getTitle().getRaw(), testPost.title(),
                "Заголовок первого найденного поста должен совпадать с созданным");
    }
}