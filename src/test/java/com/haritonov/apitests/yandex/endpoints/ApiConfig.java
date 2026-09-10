package com.haritonov.apitests.yandex.endpoints;

import com.haritonov.apitests.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Конфигурационный класс для REST Assured.
 * <p>
 * Предоставляет базовые спецификации для HTTP-запросов.
 * Содержит конфигурацию как для запросов с авторизацией, так и без нее.
 */
public class ApiConfig {

    private ApiConfig() {
    }

    /**
     * Создает и возвращает спецификацию запроса с авторизацией.
     * Включает Base URI, Content-Type и заголовок Authorization с OAuth-токеном.
     *
     * @return объект {@link RequestSpecification} для авторизованных запросов
     */
    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getYandexConfig().baseUri())
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", ConfigManager.getYandexConfig().token())
                .build();
    }

    /**
     * Создает и возвращает спецификацию запроса без авторизации.
     * Включает только Base URI и Content-Type. Использует для негативных тестов на авторизацию.
     *
     * @return объект {@link RequestSpecification} для запросов без токена.
     */
    public static RequestSpecification getNoAuthSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getYandexConfig().baseUri())
                .setContentType(ContentType.JSON)
                .build();
    }
}
