package com.haritonov.apitests.tests;

import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.steps.PostApiSteps;
import com.haritonov.apitests.utils.DataGenerator;
import com.haritonov.apitests.utils.PostAssertions;
import org.testng.annotations.Test;

public class PostsCrudTests extends BaseTest {

    @Test
    public void testCreatePostWithValidData() {
        PostRequest request = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .content(DataGenerator.generatePostContent())
                .status("draft")
                .build();

        PostResponse response = PostApiSteps.createPost(request);
        PostAssertions.assertPostCreateSuccessfully(request, response);
    }

    @Test
    public void shouldUpdatePostWhenNewTitleAndStatusSent() {
        PostRequest createRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .content(DataGenerator.generatePostContent())
                .status("draft")
                .build();
        PostResponse createResponse = PostApiSteps.createPost(createRequest);
        int postId = createResponse.getId();

        PostRequest updateRequest = PostRequest.builder()
                .title(DataGenerator.generatePostTitle())
                .status("publish")
                .build();
        PostResponse updateResponse = PostApiSteps.updatePost(postId, updateRequest);
        PostAssertions.assertPostUpdatedSuccessfully(updateRequest, updateResponse);
    }
}
