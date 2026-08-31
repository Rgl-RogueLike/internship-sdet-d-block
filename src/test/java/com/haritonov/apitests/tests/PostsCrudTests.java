package com.haritonov.apitests.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.db.DatabaseManager;
import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.steps.PostApiSteps;
import com.haritonov.apitests.utils.DataGenerator;
import com.haritonov.apitests.utils.PostAssertions;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class PostsCrudTests extends BaseTest {

    @Test(description = "TC-001: Успешное создание поста с валидными данными")
    public void shouldCreatePostWhenValidDataProvided() {
        PostRequest request = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse response = PostApiSteps.createPost(request);
        PostAssertions.assertPostCreatedSuccessfully(request, response);
    }

    @Test(description = "TC-002: Успешное редактирование поста (изменение заголовка и статуса)")
    public void shouldUpdatePostWhenNewTitleAndStatusSent() {
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        PostRequest updateRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .status(ConfigManager.getTestData().statusPublish())
                .build();
        PostResponse updateResponse = PostApiSteps.updatePost(postId, updateRequest);
        PostAssertions.assertPostUpdatedSuccessfully(updateRequest, updateResponse);
    }

    @Test(description = "TC-003: Жесткое удаление поста (с параметром force=true)")
    public void shouldDeletePostWhenForceTrue() {
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        Response deleteResponse = PostApiSteps.deletePost(postId, true);
        PostAssertions.assertPostDeletedSuccessfully(deleteResponse, postId);
    }

    @Test(description = "TC-004: Создание поста со значениями по умолчанию (только заголовок)")
    public void shouldCreatePostWithDefaultsWhenOnlyTitleProvided() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        PostRequest request = PostRequest.builder()
                .title(uniqueTitle)
                .build();
        PostResponse response = PostApiSteps.createPost(request);
        PostAssertions.assertPostCreatedWithDefaultValues(response, uniqueTitle);
    }

    @Test(description = "TC-005: Безопасное удаление (перемещение в корзину без параметра force)")
    public void shouldMovePostToTrashWhenForceNotSent() {
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        Response deleteResponse = PostApiSteps.deletePost(postId, false);
        PostAssertions.assertPostMovedToTrashSuccessfully(deleteResponse, postId);
    }

    @Test(description = "TC-006: Негативный - создание поста с невалидным статусом")
    public void shouldNotCreatePostWhenStatusInvalid() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        PostRequest request = PostRequest.builder()
                .title(uniqueTitle)
                .status(ConfigManager.getTestData().statusInvalid())
                .build();
        Response response = PostApiSteps.attemptToCreatePost(request);
        PostAssertions.assertPostNotCreatedWithInvalidStatus(response, uniqueTitle);
    }

    @Test(description = "TC-007: Негативный - редактирование несуществующего поста")
    public void shouldNotUpdatePostWhenIdInvalid() {
        int invalidPostId = DatabaseManager.getNonExistentPostId();
        PostRequest updateRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .build();

        Response response = PostApiSteps.attemptToUpdatePost(invalidPostId, updateRequest);
        PostAssertions.assertPostNotFoundWithInvalidId(response, invalidPostId);
    }

    @Test(description = "TC-008: Негативный - удаление несуществующего поста")
    public void shouldNotDeletePostWhenIdInvalid() {
        int invalidPostId = DatabaseManager.getNonExistentPostId();
        Response response = PostApiSteps.deletePost(invalidPostId, true);
        PostAssertions.assertPostNotFoundWithInvalidId(response, invalidPostId);
    }

    @Test(description = "TC-009: Негативный - обновление поста с невалидным статусом")
    public void shouldNotUpdatePostWhenStatusInvalid() {
        String initialStatus = ConfigManager.getTestData().statusDraft();
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(initialStatus);
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        PostRequest updateRequest = PostRequest.builder()
                .status(ConfigManager.getTestData().statusInvalid())
                .build();
        Response response = PostApiSteps.attemptToUpdatePost(postId, updateRequest);
        PostAssertions.assertPostNotUpdatedWithInvalidStatus(response, postId, initialStatus);
    }
}
