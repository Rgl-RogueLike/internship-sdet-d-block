package com.haritonov.apitests.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.db.PostDao;
import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.steps.PostApiSteps;
import com.haritonov.apitests.utils.DataGenerator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PostsCrudTests extends BaseTest {

    /**
     * Список для хранения ID созданных постов в рамках одного теста
     */
    private final List<Integer> createdPostIds = new ArrayList<>();

    /**
     * ID тестового поста, создаваемого в предусловии перед каждым тестом.
     */
    private int testPostId;

    /**
     * Предусловие: Создание тестового поста перед каждым тестом.
     * <p>
     * Генерирует уникальный пост со статусом "draft", отправляет запрос на его создание через API,
     * сохраняет полученный ID в переменную {@code testPostId} для использования в теле теста,
     * а также добавляет ID в список {@code createdPostIds} для последующей очистки.
     */
    @BeforeMethod
    public void setupTestPost() {
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        testPostId = createResponse.getId();
        createdPostIds.add(testPostId);
    }

    /**
     * Очистка тестовых данных после каждого теста.
     * Удаляет все созданные в рамках теста посты, чтобы не засорять БД.
     */
    @AfterMethod
    public void cleanUpTestData() {
        createdPostIds.forEach(id -> {
            try {
                PostApiSteps.deletePost(id, true);
            } catch (Exception ignored) {

            }
        });
        createdPostIds.clear();
    }

    @Test(description = "TC-001: Успешное создание поста с валидными данными")
    public void shouldCreatePostWhenValidDataProvided() {
        PostRequest request = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse response = PostApiSteps.createPost(request);
        createdPostIds.add(response.getId());

        Assert.assertTrue(response.getId() > 0, "ID поста должен быть больше 0");
        Assert.assertEquals(response.getStatus(), request.getStatus(),
                "Статус в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getTitle().getRaw(), request.getTitle(),
                "Заголовок в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getContent().getRaw(), request.getContent(),
                "Контент в ответе API должен совпадать с отправленным");

        Assert.assertEquals(PostDao.getPostStatusById(response.getId()), request.getStatus(),
                "Статус в БД должен совпадать с отправленным");
        Assert.assertEquals(PostDao.getPostTitleById(response.getId()), request.getTitle(),
                "Заголовок должен совпадать с отправленным");
        Assert.assertTrue(PostDao.getPostContentById(response.getId()).contains(request.getContent()),
                "Контент в БД должен содержать отправленный текст");
    }

    @Test(description = "TC-002: Успешное редактирование поста (изменение заголовка и статуса)")
    public void shouldUpdatePostWhenNewTitleAndStatusSent() {
        PostRequest updateRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .status(ConfigManager.getTestData().statusPublish())
                .build();
        PostResponse updateResponse = PostApiSteps.updatePost(testPostId, updateRequest);

        Assert.assertEquals(updateResponse.getStatus(), updateRequest.getStatus(),
                "Статус в ответе API должен совпадать с обновленным");
        Assert.assertEquals(updateResponse.getTitle().getRaw(), updateRequest.getTitle(),
                "Заголовок в ответе API должен совпадать с обновленным");

        Assert.assertEquals(PostDao.getPostStatusById(testPostId), updateRequest.getStatus(),
                "Статус в БД должен совпадать с обновленным");
        Assert.assertEquals(PostDao.getPostTitleById(testPostId), updateRequest.getTitle(),
                "Заголовок в БД должен совпадать с обновленным");
    }

    @Test(description = "TC-003: Жесткое удаление поста (с параметром force=true)")
    public void shouldDeletePostWhenForceTrue() {
        Response deleteResponse = PostApiSteps.deletePost(testPostId, true);

        Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.SC_OK,
                "Статус код удаления должен быть 200");
        Assert.assertTrue(deleteResponse.jsonPath().getBoolean("deleted"),
                "Поле 'deleted' в ответе должно быть true");
        Assert.assertEquals(deleteResponse.jsonPath().getInt("previous.id"), testPostId,
                "Поле 'previous.id' должно совпадать с ID удаленного поста");

        Assert.assertFalse(PostDao.isPostExists(testPostId),
                "Пост не должен существовать в БД после удаления с force=true");
    }

    @Test(description = "TC-004: Создание поста со значениями по умолчанию (только заголовок)")
    public void shouldCreatePostWithDefaultsWhenOnlyTitleProvided() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        PostRequest request = PostRequest.builder()
                .title(uniqueTitle)
                .build();
        PostResponse response = PostApiSteps.createPost(request);
        createdPostIds.add(response.getId());

        Assert.assertTrue(response.getId() > 0,
                "ID поста должен быть больше 0");
        Assert.assertEquals(response.getStatus(), ConfigManager.getTestData().statusDraft(),
                "Статус по умолчанию должен быть 'draft'");
        Assert.assertEquals(response.getTitle().getRaw(), uniqueTitle,
                "Заголовок в ответе API должен совпадать с отправленным");
        Assert.assertEquals(response.getContent().getRaw(), "",
                "Контент по умолчанию должен быть пустой строкой");

        Assert.assertEquals(PostDao.getPostStatusById(response.getId()), ConfigManager.getTestData().statusDraft(),
                "Статус в БД по умолчанию должен быть 'draft'");
        Assert.assertEquals(PostDao.getPostTitleById(response.getId()), uniqueTitle,
                "Заголовок в БД должен совпадать с отправленным");
        String dbContent = PostDao.getPostContentById(response.getId());
        Assert.assertTrue(dbContent == null || dbContent.isEmpty(),
                "Контент в БД должен быть пустым");
    }

    @Test(description = "TC-005: Безопасное удаление (перемещение в корзину без параметра force)")
    public void shouldMovePostToTrashWhenForceNotSent() {
        Response deleteResponse = PostApiSteps.deletePost(testPostId, false);

        Assert.assertEquals(deleteResponse.getStatusCode(), HttpStatus.SC_OK,
                "Статус код удаления должен быть 200");
        Assert.assertEquals(deleteResponse.jsonPath().getInt("id"), testPostId,
                "ID в ответе должен совпадать с удаляемым постом");
        Assert.assertEquals(deleteResponse.jsonPath().getString("status"),
                ConfigManager.getTestData().statusTrash(),
                "Статус в ответе API должен быть 'trash'");

        Assert.assertTrue(PostDao.isPostExists(testPostId),
                "Пост должен физически оставаться в БД после перемещения в корзину");
        Assert.assertEquals(PostDao.getPostStatusById(testPostId),
                ConfigManager.getTestData().statusTrash(),
                "Статус поста в БД должен быть изменен на 'trash'");
    }

    @Test(description = "TC-006: Негативный - создание поста с невалидным статусом")
    public void shouldNotCreatePostWhenStatusInvalid() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        PostRequest request = PostRequest.builder()
                .title(uniqueTitle)
                .status(ConfigManager.getTestData().statusInvalid())
                .build();
        Response response = PostApiSteps.attemptToCreatePost(request);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorInvalidParam(),
                "Код ошибки должен быть rest_invalid_param");

        Assert.assertEquals(PostDao.getPostCountByTitle(uniqueTitle), 0,
                "Пост с невалидным статусом не должен создаваться в БД");
    }

    @Test(description = "TC-007: Негативный - редактирование несуществующего поста")
    public void shouldNotUpdatePostWhenIdInvalid() {
        int invalidPostId = PostDao.getNonExistentPostId();
        PostRequest updateRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .build();

        Response response = PostApiSteps.attemptToUpdatePost(invalidPostId, updateRequest);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorPostInvalidId(),
                "Код ошибки должен быть rest_post_invalid_id");

        Assert.assertFalse(PostDao.isPostExists(invalidPostId),
                "Поста с невалидным ID не должно существовать в БД");

    }

    @Test(description = "TC-008: Негативный - удаление несуществующего поста")
    public void shouldNotDeletePostWhenIdInvalid() {
        int invalidPostId = PostDao.getNonExistentPostId();
        Response response = PostApiSteps.deletePost(invalidPostId, true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorPostInvalidId(),
                "Код ошибки должен быть rest_post_invalid_id");

        Assert.assertFalse(PostDao.isPostExists(invalidPostId),
                "Поста с невалидным ID не должно существовать в БД");
    }

    @Test(description = "TC-009: Негативный - обновление поста с невалидным статусом")
    public void shouldNotUpdatePostWhenStatusInvalid() {
        String initialStatus = ConfigManager.getTestData().statusDraft();
        PostRequest updateRequest = PostRequest.builder()
                .status(ConfigManager.getTestData().statusInvalid())
                .build();
        Response response = PostApiSteps.attemptToUpdatePost(testPostId, updateRequest);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("code"),
                ConfigManager.getTestData().errorInvalidParam(),
                "Код ошибки должен быть rest_invalid_param");

        Assert.assertEquals(PostDao.getPostStatusById(testPostId), initialStatus,
                "Статус поста в БД не должен измениться после неудачного обновления");
    }
}
