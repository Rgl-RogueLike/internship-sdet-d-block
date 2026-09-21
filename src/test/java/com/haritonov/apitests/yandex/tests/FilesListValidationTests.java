package com.haritonov.apitests.yandex.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.yandex.dto.response.LinkResponse;
import com.haritonov.apitests.yandex.endpoints.Schemas;
import com.haritonov.apitests.yandex.steps.ResourceSteps;
import com.haritonov.apitests.yandex.utils.DataGenerator;
import com.haritonov.apitests.yandex.utils.FileUtils;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FilesListValidationTests {

    private File firstLocalFile;
    private File secondLocalFile;
    private String testFolderPath;

    /**
     * Предусловие: Создание тестовой папки и загрузка в нее файла.
     */
    @BeforeMethod
    public void setUpFileForSchemaTest() {
        String firstFileName = ConfigManager.getYandexTestData().fileName();
        String firstFileContent = ConfigManager.getYandexTestData().fileContent();

        String secondFileName = ConfigManager.getYandexTestData().fileNameSecond();
        String secondFileContent = ConfigManager.getYandexTestData().fileContentSecond();

        testFolderPath = DataGenerator.generateUniqueFolderPath();
        String firstTestFilePath = testFolderPath + "/" + firstFileName;
        String secondTestFilePath = testFolderPath + "/" + secondFileName;

        ResourceSteps.clearTrash();
        ResourceSteps.createFolder(testFolderPath);

        firstLocalFile = FileUtils.createTempFile(firstFileName, firstFileContent);
        LinkResponse firstUploadLink = ResourceSteps.getUploadLink(firstTestFilePath, true);
        ResourceSteps.uploadFileToDisk(firstUploadLink.getHref(), firstLocalFile);

        secondLocalFile = FileUtils.createTempFile(secondFileName, secondFileContent);
        LinkResponse secondUploadLink = ResourceSteps.getUploadLink(secondTestFilePath, true);
        ResourceSteps.uploadFileToDisk(secondUploadLink.getHref(), secondLocalFile);
    }

    /**
     * Постусловие: Очистка тестовых данных.
     */
    @AfterMethod
    public void cleanUpFileForSchemaTest() {
        ResourceSteps.safeDeleteFolder(testFolderPath);
        FileUtils.deleteFile(firstLocalFile);
        FileUtils.deleteFile(secondLocalFile);
        ResourceSteps.clearTrash();
    }

    @Test(description = "TC-001: Получение списка файлов и валидация JSON Schema")
    public void shouldGetFilesListAndValidateSchema() {
        Response response = ResourceSteps.getFilesList();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK,
                "Статус код должен быть 200 OK");

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Schemas.FILES_LIST));
    }
}
