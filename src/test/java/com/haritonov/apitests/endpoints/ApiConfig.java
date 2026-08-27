package com.haritonov.apitests.endpoints;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.config.Configuration;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {

    private ApiConfig() {

    }

    public static RequestSpecification getBaseSpec() {
        Configuration config = ConfigManager.getConfig();

        return new RequestSpecBuilder()
                .setBaseUri(config.baseUri())
                .setBasePath(config.basePath())
                .setContentType(ContentType.JSON)
                .setAuth(RestAssured.basic(config.wpUsername(), config.wpPassword()))
                .build();
    }
}
