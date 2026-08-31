package com.haritonov.apitests.utils;

import com.github.javafaker.Faker;
import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.dto.request.PostRequest;

/**
 * Утилитный класс для генерации случайных тестовых данных.
 * <p>
 * Использует библиотеку JavaFaker для создания уникальных и осмысленных значений,
 * чтобы избежать конфликтов в базе данных при многократных запусках тестов.
 */
public final class DataGenerator {

    private static final Faker faker = new Faker();

    private DataGenerator() {
    }

    /**
     * Генерирует уникальный заголовок для поста.
     *
     * @return строка со случайным заголовком
     */
    public static String generatePostTitle() {
        return "Test Post: " + faker.book().title()+ " - " + System.currentTimeMillis();
    }

    /**
     * Генерирует уникальный контент для поста.
     * Размер генерируемого текста (количество предложений) берется из конфигурационного файла.
     *
     * @return строка со сгенерированным текстом
     */
    public static String generatePostContent() {
        int paragraphSize = ConfigManager.getTestData().paragraphSize();
        return faker.lorem().paragraph(paragraphSize);
    }

    /**
     * Создает готовый объект {@link PostRequest} с заполненными уникальными заголовком и контентом.
     *
     * @param status статус, который будет присвоен посту (например, "draft", "publish")
     * @return объект {@link PostRequest}, готовый к отправке в API
     */
    public static PostRequest generateDefaultPostRequest(String status) {
        return PostRequest.builder()
                .title(generatePostTitle())
                .content(generatePostContent())
                .status(status)
                .build();
    }
}
