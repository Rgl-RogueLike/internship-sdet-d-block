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

    @Test(description = "TC-012: Поиск постов по контенту")
    public void shouldFindPostWhenSearchByContent() {
        PostsDbHelper.DbTestPost testPost = PostsDbHelper.createTestPostInDb(ConfigManager.getTestData().statusPublish());
        dbCreatedPostIds.add(testPost.id());

        Response response = PostApiSteps.searchPosts(testPost.content());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");
        PostResponse[] foundPostsArray = response.as(PostResponse[].class);
        List<PostResponse> foundPosts = Arrays.asList(foundPostsArray);

        Assert.assertFalse(foundPosts.isEmpty(),
                "Массив найденных постов не должен быть пустым");
        Assert.assertEquals(foundPosts.getFirst().getId(), testPost.id(),
                "ID первого найденного поста должен совпадать с созданным");
        Assert.assertEquals(foundPosts.getFirst().getContent().getRaw(), testPost.content(),
                "Заголовок первого найденного поста должен совпадать с созданным");
    }

    @Test(description = "ТС-013: Фильтрация постов по валидному статусу")
    public void shouldFilterPostsWhenStatusIsValid() {
        String status = ConfigManager.getTestData().statusDraft();
        PostsDbHelper.DbTestPost testPost = PostsDbHelper.createTestPostInDb(status);
        dbCreatedPostIds.add(testPost.id());

        Response response = PostApiSteps.getPostsByStatusAndSearch(status, testPost.title());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");
        PostResponse[] foundPostsArray = response.as(PostResponse[].class);
        List<PostResponse> foundPosts = Arrays.asList(foundPostsArray);

        Assert.assertFalse(foundPosts.isEmpty(),
                "Массив найденных постов не должен быть пустым");
        Assert.assertEquals(foundPosts.getFirst().getId(), testPost.id(),
                "ID первого найденного поста должен совпадать с созданным");
        Assert.assertEquals(foundPosts.getFirst().getStatus(), status,
                "Заголовок первого найденного поста должен совпадать с созданным");
    }

    @Test(description = "TC-014: Получение данных несуществующего поста")
    public void shouldNotGetPostWhenIdInvalid() {
        int invalidPostId = PostDao.getNonExistentPostId();
        Response response = PostApiSteps.getPost(invalidPostId);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorPostInvalidId(),
                "Код ошибки должен быть rest_post_invalid_id");
    }

    @Test(description = "TC-015: Фильтрация постов по невалидному статусу")
    public void shouldNotFilterPostsWhenStatusInvalid() {
        Response response = PostApiSteps.getPostsByStatus(ConfigManager.getTestData().statusInvalid());

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorInvalidParam(),
                "Код ошибки должен быть rest_invalid_param");
    }

    @Test(description = "TC-016: Получение поста с невалидным форматом ID")
    public void shouldNotGetPostIdFormatString() {
        Response response = PostApiSteps.getPostByStringId("invalid_string");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorNoRoute(),
                "");
    }
}