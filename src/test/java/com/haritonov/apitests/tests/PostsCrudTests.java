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

    @Test
    public void shouldCreatePostWhenValidDataProvided() {
        PostRequest request = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse response = PostApiSteps.createPost(request);
        PostAssertions.assertPostCreateSuccessfully(request, response);
    }

    @Test
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

    @Test
    public void shouldDeletePostWhenForceTrue() {
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        Response deleteResponse = PostApiSteps.deletePost(postId, true);
        PostAssertions.assertPostDeletedSuccessfully(deleteResponse, postId);
    }

    @Test
    public void shouldCreatePostWithDefaultsWhenOnlyTitleProvided() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        PostRequest request = PostRequest.builder()
                .title(uniqueTitle)
                .build();
        PostResponse response = PostApiSteps.createPost(request);
        PostAssertions.assertPostCreatedWithDefaultValues(response, uniqueTitle);
    }

    @Test
    public void shouldMovePostToTrashWhenForceNotSent() {
        PostRequest createRequest = DataGenerator.generateDefaultPostRequest(ConfigManager.getTestData().statusDraft());
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        Response deleteResponse = PostApiSteps.deletePost(postId, false);
        PostAssertions.assertPostMovedToTrashSuccessfully(deleteResponse, postId);
    }

    @Test
    public void shouldNotCreatePostWhenStatusInvalid() {
        String uniqueTitle = DataGenerator.generatePostTitle();
        PostRequest request = PostRequest.builder()
                .title(uniqueTitle)
                .status(DataGenerator.generatePostTitle())
                .build();
        Response response = PostApiSteps.attemptToCreatePost(request);
        PostAssertions.assertPostNotCreateWithInvalidStatus(response, uniqueTitle);
    }

    @Test
    public void shouldNotUpdatePostWhenIdInvalid() {
        int invalidPostId = DatabaseManager.getNonExistentPostId();
        PostRequest updateRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .build();

        Response response = PostApiSteps.attemptToUpdatePost(invalidPostId, updateRequest);
        PostAssertions.assertPostNotFoundWithInvalidId(response, invalidPostId);
    }

    @Test
    public void shouldNotDeletePostWhenIdInvalid() {
        int invalidPostId = DatabaseManager.getNonExistentPostId();
        Response response = PostApiSteps.deletePost(invalidPostId, true);
        PostAssertions.assertPostNotFoundWithInvalidId(response, invalidPostId);
    }
}
