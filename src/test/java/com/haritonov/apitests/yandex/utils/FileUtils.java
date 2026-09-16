package com.haritonov.apitests.yandex.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Утилитный класс для работы с локальной файловой системой.
 * <p>
 * Отвечает за создание временных тестовых файлов в системной директории temp
 * и их безопасное удаление после прохождения тестов.
 */
public final class FileUtils {

    private FileUtils() { }

    /**
     * Создает временный текстовый файл с указанным именем и содержимым.
     * Файл создается в системной директории temp
     *
     * @param fileName Имя файла
     * @param content Текстовое содержимое файла
     * @return Объект {@link File}, указывающий на созданный файл
     */
    public static File createTempFile(String fileName, String content) {
        try {
            Path tempDir = Path.of(System.getProperty("java.io.tmpdir"));
            Path filePath = tempDir.resolve(fileName);
            Files.writeString(filePath, content);
            return filePath.toFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file: " + fileName, e);
        }
    }

    /**
     * Безопасно удаляет локальный файл, если он существует.
     *
     * @param file Объект {@link File} для удаления
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete temp file: " + file.getName());
            }
        }
    }
}
