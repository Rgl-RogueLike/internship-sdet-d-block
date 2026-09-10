package com.haritonov.apitests.yandex.steps;

import com.haritonov.apitests.yandex.dto.response.DiskResponse;
import com.haritonov.apitests.yandex.dto.response.ErrorResponse;
import com.haritonov.apitests.yandex.endpoints.ApiConfig;
import com.haritonov.apitests.yandex.endpoints.Endpoints;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

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
    public static DiskResponse getDiskInfoWithToken() {
        return given()
                .spec(ApiConfig.getBaseSpec())
                .when()
                .get(Endpoints.DISK)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(DiskResponse.class);
    }

    /**
     * Шаг: Попытка получения информации о диске без передачи токена авторизации.
     *
     * @return объект {@link Response} от сервера.
     */
    public static ErrorResponse getDiskInfoWithoutToken() {
        return given()
                .spec(ApiConfig.getNoAuthSpec())
                .when()
                .get(Endpoints.DISK)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .as(ErrorResponse.class);
    }
}
