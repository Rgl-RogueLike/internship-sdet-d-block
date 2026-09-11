package com.haritonov.apitests.yandex.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.yandex.dto.response.ErrorResponse;
import com.haritonov.apitests.yandex.dto.response.LinkResponse;
import com.haritonov.apitests.yandex.steps.ResourceSteps;
import com.haritonov.apitests.yandex.utils.DataGenerator;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
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

    @Test(description = "TC-002: Создание папки по уже существующему пути")
    public void shouldNotCreateFolderWhenPathAlreadyExists() {
        testFolderPath = DataGenerator.generateUniqueFolderPath();
        ResourceSteps.createFolder(testFolderPath);
        Response response = ResourceSteps.attemptToCreateFolder(testFolderPath);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CONFLICT,
                "Статус должен быть 409 Conflict");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorExistentDirectory(),
                "Код ошибки должен быть DiskPathPointsToExistentDirectoryError");
    }

    @Test(description = "TC-003: Создание папки по несуществующему пути")
    public void shouldNotCreateFolderWhenParentPathDoesNotExist() {
        String invalidPath = DataGenerator.generatePathWithNonExistentParent();
        Response response = ResourceSteps.attemptToCreateFolder(invalidPath);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CONFLICT,
                "Статус код должен быть 409 Conflict");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorPathDoesntExist(),
                "Код ошибки должен быть DiskPathDoesntExistsError");
    }

    @Test(description = "TC-004: Запрос с пустым значением параметра path")
    public void shouldNotCreateFolderWhenPathIsEmpty() {
        Response response = ResourceSteps.attemptToCreateFolder(StringUtils.EMPTY);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorFieldValidation(),
                "Код ошибки должен быть FieldValidationError");
    }

    @Test(description = "TC-005: Запрос без параметра path")
    public void shouldNotCreateFolderWhenPathParamMissing() {
        Response response = ResourceSteps.attemptToCreateFolderWithoutPathParam();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorFieldValidation(),
                "Код ошибки должен быть FieldValidationError");
    }

    @Test(description = "TC-006: Успешное удаление папки")
    public void shouldDeleteFolderWhenItExists() {
        testFolderPath = DataGenerator.generateUniqueFolderPath();
        ResourceSteps.createFolder(testFolderPath);

        ResourceSteps.deleteFolder(testFolderPath);

        Response getResponse = ResourceSteps.getFolderInfoResponse(testFolderPath);
        Assert.assertEquals(getResponse.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
    }

    @Test(description = "TC-007: Удаление несуществующей папки")
    public void shouldNotDeleteFolderWhenItDoesNotExist() {
        String nonExistentPath = DataGenerator.generateUniqueFolderPath();
        Response response = ResourceSteps.attemptToDeleteFolder(nonExistentPath);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorNotFound(),
                "Код ошибки должен быть DiskNotFoundError");
    }

    @Test(description = "TC-008: Удаление папки с пустым значением параметра path")
    public void shouldNotDeleteFolderWhenPathIsEmpty() {
        Response response = ResourceSteps.attemptToDeleteFolder(StringUtils.EMPTY);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST,
                "Статус код должен быть 400 Bad Request");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorFieldValidation(),
                "Код ошибки должен быть FieldValidationError");
    }

    @Test(description = "TC-009: Удаление уже удаленной папки (которая лежит в корзине)")
    public void shouldNotDeleteFolderWhenItIsAlreadyInTrash() {
        testFolderPath = DataGenerator.generateUniqueFolderPath();
        ResourceSteps.createFolder(testFolderPath);
        ResourceSteps.deleteFolder(testFolderPath);
        Response response = ResourceSteps.attemptToDeleteFolder(testFolderPath);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND,
                "Статус код должен быть 404 Not Found");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        Assert.assertEquals(errorResponse.getError(), ConfigManager.getYandexTestData().errorNotFound(),
                "Код ошибки должен быть DiskNotFoundError");
    }

    @Test(description = "TC-010: Успешное восстановление папки из корзины")
    public void shouldRestoreFolderFromTrashSuccessfully() {
        ResourceSteps.clearTrash();
        testFolderPath = DataGenerator.generateUniqueFolderPath();
        ResourceSteps.createFolder(testFolderPath);
        ResourceSteps.deleteFolder(testFolderPath);

        String trashedPath = ResourceSteps.findTrashedFolderPathByOrigin(testFolderPath);
        Assert.assertNotNull(trashedPath, "Путь удаленной папки в корзине не должен быть null");
        LinkResponse restoreResponse = ResourceSteps.restoreFolderFromTrash(trashedPath);
        Assert.assertNotNull(restoreResponse.getHref(), "Поле href в ответе восстановления не должно быть null");
        Response getResponse = ResourceSteps.getFolderInfoResponse(testFolderPath);
        Assert.assertEquals(getResponse.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 ОК");
    }
}
