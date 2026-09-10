package com.haritonov.apitests.yandex.tests;

import com.haritonov.apitests.common.BaseTest;
import com.haritonov.apitests.yandex.dto.response.DiskResponse;
import com.haritonov.apitests.yandex.dto.response.ErrorResponse;
import com.haritonov.apitests.yandex.steps.DiskSteps;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Сьют тестов: Авторизация в API Яндекс.Диска.
 */
public class DiskAuthTests extends BaseTest {

    @Test(description = "TC-001: Авторизация с валидным токеном")
    public void shouldGetDiskInfoWhenValidTokenProvided() {
        DiskResponse diskResponse = DiskSteps.getDiskInfoWithToken();
        Assert.assertNotNull(diskResponse.getUser(),
                "Объект user не должен быть null");
        Assert.assertNotNull(diskResponse.getUser().getLogin(),
                "Поле login не должно быть null");
        Assert.assertFalse(diskResponse.getUser().getLogin().isEmpty(),
                "Поле login не должно быть пустым");
        Assert.assertNotNull(diskResponse.getUser().getDisplayName(),
                "Поле display_name не должно быть null");
        Assert.assertFalse(diskResponse.getUser().getDisplayName().isEmpty(),
                "Поле display_name не должно быть пустым");
    }

    @Test(description = "TC-002: Авторизация без токена")
    public void shouldNotGetDiskInfoWhenNoTokenProvided() {
        ErrorResponse errorResponse = DiskSteps.getDiskInfoWithoutToken();

        Assert.assertNotNull(errorResponse.getError(),
                "Поле error не должно быть null");
        Assert.assertFalse(errorResponse.getError().isEmpty(),
                "Поле error не должно быть пустым");
        Assert.assertNotNull(errorResponse.getDescription(),
                "Поле description не должно быть null");
        Assert.assertFalse(errorResponse.getDescription().isEmpty(),
                "Поле description не должно быть пустым");
        Assert.assertNotNull(errorResponse.getMessage(),
                "Поле message не должно быть null");
        Assert.assertFalse(errorResponse.getMessage().isEmpty(),
                "Поле message не должно быть пустым");
    }
}
