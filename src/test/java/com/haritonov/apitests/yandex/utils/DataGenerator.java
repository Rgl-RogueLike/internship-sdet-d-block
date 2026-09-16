package com.haritonov.apitests.yandex.utils;

import com.haritonov.apitests.config.ConfigManager;

/**
 * Утилитный класс для генерации тестовых данных для Яндекс.Диска.
 */
public final class DataGenerator {

    private DataGenerator() {
    }

    /**
     * Генерирует уникальный путь для папки на Диске.
     *
     * @return строка вида "disk:/test_folder_123456789"
     */
    public static String generateUniqueFolderPath() {
        String folderPrefix = ConfigManager.getYandexTestData().testFolderPrefix();
        String diskPrefix = ConfigManager.getYandexTestData().diskPrefix();
        return diskPrefix + folderPrefix + System.currentTimeMillis();
    }

    /**
     * Генерирует несуществующий путь на Диске.
     *
     * @return строка вида "non_existent_folder/test_folder_123456789"
     */
    public static String generatePathWithNonExistentParent() {
        String parent = ConfigManager.getYandexTestData().nonExistentParent();
        String folderPrefix = ConfigManager.getYandexTestData().testFolderPrefix();
        return parent + "/" + folderPrefix + System.currentTimeMillis();
    }

    /**
     * Формирует полный путь файла на Диске, склеивая префикс, имя папки и имя файла.
     *
     * @param folderName Имя папки
     * @param fileName Имя файла
     * @return Полный путь в виде строки
     */
    public static String generateDiskFilePath(String folderName, String fileName) {
        return ConfigManager.getYandexTestData().diskPrefix() + folderName + "/" + fileName;
    }

    /**
     * Формирует полный путь папки на диске
     *
     * @param folderName Имя папки
     * @return Полный путь в виде строки
     */
    public static String generateDiskFolderPath(String folderName) {
        return ConfigManager.getYandexTestData().diskPrefix() + folderName;
    }
}
