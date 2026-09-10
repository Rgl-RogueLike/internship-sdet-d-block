package com.haritonov.apitests.yandex.utils;

import com.haritonov.apitests.config.ConfigManager;

/**
 * Утилитный класс для генерации тестовых данных для Яндекс.Диска.
 */
public final class DataGenerator {

    private DataGenerator() { }

    /**
     * Генерирует уникальный путь для папки на Диске.
     * @return строка вида "/test_folder_123456789"
     */
    public static String generateUniqueFolderPath() {
        String folderPrefix = ConfigManager.getYandexTestData().testFolderPrefix();
        return folderPrefix + System.currentTimeMillis();
    }
}
