package com.haritonov.apitests.endpoints;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.config.Configuration;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Конфигурационный класс для REST Assured.
 * <p>
 * Предоставляет базовую спецификацию ({@link RequestSpecification}) для всех API-запросов.
 * Включает в себя Base URI, Base Path, Content-Type и настройку Preemptive Basic-аутентификации.
 */
public class ApiConfig {

    private ApiConfig() {

    }

    /**
     * Создает и возвращает базовую спецификацию запроса.
     *
     * @return объект {@link RequestSpecification}
     */
    public static RequestSpecification getBaseSpec() {
        Configuration config = ConfigManager.getConfig();

        return new RequestSpecBuilder()
                .setBaseUri(config.baseUri())
                .setBasePath(config.basePath())
                .setContentType(ContentType.JSON)
                .setAuth(RestAssured.preemptive().basic(config.wpUsername(), config.wpPassword()))
                .build();
    }
}
