package com.haritonov.apitests.yandex.steps;

import com.haritonov.apitests.yandex.endpoints.ApiConfig;
import com.haritonov.apitests.yandex.endpoints.Endpoints;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Класс шагов для взаимодействия с API Яндекс.Диска.
 */
public final class DiskSteps {

    private DiskSteps() {
    }

    /**
     * Шаг: Получение информации о диске с использованием валидного OAuth-токена.
     *
     * @return объект {@link Response} от сервера
     */
    public static Response getDiskInfoWithToken() {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when()
                .get(Endpoints.DISK)
                .then()
                .extract()
                .response();
    }
}
