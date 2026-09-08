package com.haritonov.apitests.yandex.tests;

import com.haritonov.apitests.common.BaseTest;
import com.haritonov.apitests.yandex.dto.response.DiskResponse;
import com.haritonov.apitests.yandex.steps.DiskSteps;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Сьют тестов: Авторизация в API Яндекс.Диска.
 */
public class DiskAuthTests extends BaseTest {

    @Test(description = "TC-001: Авторизация с валидным токеном")
    public void shouldGetDiskInfoWhenValidTokenProvided() {
        Response response = DiskSteps.getDiskInfoWithToken();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");
        DiskResponse diskResponse = response.as(DiskResponse.class);
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
}
