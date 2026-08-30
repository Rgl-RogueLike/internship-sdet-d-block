package com.haritonov.apitests.utils;

import com.github.javafaker.Faker;
import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.dto.request.PostRequest;

public final class DataGenerator {

    private static final Faker faker = new Faker();

    private DataGenerator() {}

    public static String generatePostTitle() {
        return "Test Post: " + faker.book().title();
    }

    public static String generatePostContent() {
        int paragraphSize = ConfigManager.getTestData().paragraphSize();
        return faker.lorem().paragraph(paragraphSize);
    }

    public static PostRequest generateDefaultPostRequest(String status) {
        return PostRequest.builder()
                .title(generatePostTitle())
                .content(generatePostContent())
                .status(status)
                .build();
    }
}
