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
}
