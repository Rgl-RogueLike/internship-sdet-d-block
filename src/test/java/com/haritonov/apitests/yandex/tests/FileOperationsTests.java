package com.haritonov.apitests.yandex.tests;

import com.haritonov.apitests.config.ConfigManager;
import com.haritonov.apitests.yandex.dto.response.ErrorResponse;
import com.haritonov.apitests.yandex.dto.response.FileResponse;
import com.haritonov.apitests.yandex.dto.response.LinkResponse;
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

/**
 * Сьют тестов: Операции с файлами в Яндекс.Диске.
 * <p>
 * Тестовые данные создаются в предусловиях и удаляются в постусловиях.
 */
public class FileOperationsTests {

    /**
     * Локальный временный файл, для загрузки на Диск.
     */
    private File localFile;

    /**
     * Полный путь к папке "input_data" на Диске.
     */
    private String inputFolderPath;

    /**
     * Полный путь к папке "output_data" на Диске
     */
    private String outputFolderPath;

    /**
     * Полный путь к загружаемому файлу на Диске
     */
    private String diskFilePath;

    /**
     * Полный путь к папке "sdet-data" на Диске
     */
    private String sdetFolderPath;

    /**
     * Полный путь к загружаемому и скачиваемому файлу на диске.
     */
    private String sdetFilePath;

    /**
     * Предусловие: Подготовка окружения перед каждым тестом.
     * <p>
     * Очищает корзину, создает тестовые папки на Диске и генерирует локальный тестовый файл.
     */
    @BeforeMethod
    public void setUpFileTest() {
        String fileName = ConfigManager.getYandexTestData().fileName();
        String fileContent = ConfigManager.getYandexTestData().fileContent();
        String inputFolderName = ConfigManager.getYandexTestData().folderInput();
        String outputFolderName = ConfigManager.getYandexTestData().folderOutput();
        String sdetFolderName = ConfigManager.getYandexTestData().folderSdet();

        inputFolderPath = DataGenerator.generateDiskFolderPath(inputFolderName);
        outputFolderPath = DataGenerator.generateDiskFolderPath(outputFolderName);
        sdetFolderPath = DataGenerator.generateDiskFolderPath(sdetFolderName);

        diskFilePath = DataGenerator.generateDiskFilePath(inputFolderName, fileName);
        sdetFilePath = DataGenerator.generateDiskFilePath(sdetFolderName, fileName);

        ResourceSteps.clearTrash();
        ResourceSteps.createFolder(inputFolderPath);
        ResourceSteps.createFolder(outputFolderPath);
        ResourceSteps.createFolder(sdetFolderPath);

        localFile = FileUtils.createTempFile(fileName, fileContent);
        LinkResponse uploadLink = ResourceSteps.getUploadLink(sdetFilePath, false);
        ResourceSteps.uploadFileToDisk(uploadLink.getHref(), localFile);
    }

    /**
     * Постусловие: Очистка тестовых данных после каждого теста.
     */
    @AfterMethod
    public void cleanUpFileTest() {
        ResourceSteps.safeDeleteFolder(inputFolderPath);
        ResourceSteps.safeDeleteFolder(outputFolderPath);
        ResourceSteps.safeDeleteFolder(sdetFolderPath);
        ResourceSteps.clearTrash();
        FileUtils.deleteFile(localFile);
    }

    @Test(description = "TC-001: Загрузка и копирование файла")
    public void shouldUploadAndCopyFileSuccessfully() {
        LinkResponse uploadLink = ResourceSteps.getUploadLink(diskFilePath, false);
        ResourceSteps.uploadFileToDisk(uploadLink.getHref(), localFile);

        String copyToPath = DataGenerator.generateDiskFilePath(
                ConfigManager.getYandexTestData().folderOutput(),
                ConfigManager.getYandexTestData().fileName()
        );

        Response copyResponse = ResourceSteps.attemptToCopyFile(diskFilePath, copyToPath);
        Assert.assertEquals(copyResponse.getStatusCode(), HttpStatus.SC_CREATED,
                "Статус код должен быть 201 Created");
        LinkResponse copyLink = copyResponse.as(LinkResponse.class);
        Assert.assertNotNull(copyLink.getHref(), "Ссылка на скопированный файл не должна быть null");
        Response getResponse = ResourceSteps.getFolderInfoResponse(copyToPath);
        FileResponse fileResponse = getResponse.as(FileResponse.class);
        Assert.assertNotNull(fileResponse.getName(), "Поле name не должно быть null");
        Assert.assertNotNull(fileResponse.getMimeType(), "Поле mime_type не должно быть null");
        Assert.assertNotNull(fileResponse.getMediaType(), "Поле media_type не должно быть null");

        Response conflictResponse = ResourceSteps.attemptToCopyFile(diskFilePath, copyToPath);
        Assert.assertEquals(conflictResponse.getStatusCode(), HttpStatus.SC_CONFLICT,
                "Статус код должен быть 409 Conflict");
        ErrorResponse errorResponse = conflictResponse.as(ErrorResponse.class);
        Assert.assertNotNull(errorResponse.getError(), "Поле error не должно быть null");
        Assert.assertNotNull(errorResponse.getDescription(), "Поле description не должно быть null");
        Assert.assertNotNull(errorResponse.getMessage(), "Поле message не должно быть null");
    }

    @Test(description = "TC-002: Скачивание текстового файла")
    public void shouldDownloadFileAndCompareContent() {
        LinkResponse downloadLink = ResourceSteps.getDownloadLink(sdetFilePath);
        Assert.assertNotNull(downloadLink.getHref(), "Поле href в ответе не должно быть null");

        String downloadContent = ResourceSteps.downloadFileContent(downloadLink.getHref());
        String expectedContent = ConfigManager.getYandexTestData().fileContent();
        Assert.assertEquals(downloadContent, expectedContent,
                "Содержимое скачанного файла должно совпадать с оригиналом");
    }
}
