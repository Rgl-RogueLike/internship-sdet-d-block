package com.haritonov.apitests.steps;

import com.haritonov.apitests.dto.request.PostRequest;
import com.haritonov.apitests.dto.response.PostResponse;
import com.haritonov.apitests.endpoints.ApiConfig;
import com.haritonov.apitests.endpoints.ApiEndpoints;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public final class PostApiSteps {

    private PostApiSteps() {}

    public static PostResponse createPost(PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POSTS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(PostResponse.class);
    }

    public static Response deletePost(int id, boolean force) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .queryParam("force", force)
                .when()
                .delete(ApiEndpoints.POST_BY_ID, id)
                .then()
                .extract()
                .response();
    }

    public static Response getPost(int id) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when()
                .get(ApiEndpoints.POST_BY_ID, id)
                .then()
                .extract()
                .response();
    }

    public static PostResponse updatePost(int id , PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POST_BY_ID, id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(PostResponse.class);
    }

    public static Response attemptToCreatePost(PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POSTS)
                .then()
                .extract()
                .response();
    }

    public static Response attemptToUpdatePost(int id, PostRequest postRequest) {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .body(postRequest)
                .when()
                .post(ApiEndpoints.POST_BY_ID, id)
                .then()
                .extract()
                .response();
    }
}
