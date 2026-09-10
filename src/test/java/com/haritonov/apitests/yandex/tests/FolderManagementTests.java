package com.haritonov.apitests.yandex.tests;

import com.haritonov.apitests.yandex.dto.response.LinkResponse;
import com.haritonov.apitests.yandex.steps.ResourceSteps;
import com.haritonov.apitests.yandex.utils.DataGenerator;
import io.restassured.http.Method;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Сьют тестов: Управление папками в Яндекс.Диске.
 */
public class FolderManagementTests {

    private String testFolderPath;

    /**
     * Постусловие: Очистка тестовых данных после каждого теста.
     * Удаляет папку с Диска и очищает корзину.
     */
    @AfterMethod
    public void cleanUpYandexDisk() {
        if (testFolderPath != null) {
            ResourceSteps.safeDeleteFolder(testFolderPath);
        }
        ResourceSteps.clearTrash();
    }

    @Test(description = "ТС-001: Успешное создание папки")
    public void shouldCreateFolderWhenValidProvided() {
        testFolderPath = DataGenerator.generateUniqueFolderPath();
        LinkResponse response = ResourceSteps.createFolder(testFolderPath);

        Assert.assertNotNull(response.getHref(), "Поле href не должно быть null");
        Assert.assertEquals(response.getMethod(), Method.GET.name(), "Поле method должно быть GET");
        Assert.assertFalse(response.isTemplated(), "Поле templated должно быть false");
    }
}
