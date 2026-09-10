package com.haritonov.apitests.yandex.config;

import org.aeonbits.owner.Config;

/**
 * Интерфейс тестовых данных для API Яндекс.Диска.
 * <p>
 * Связывает свойства из файла {@code yandex-test-data.properties}.
 * Содержит префиксы для генерации путей, тестовые имена и коды ожидаемых ошибок API.
 */
@Config.Sources("classpath:configurations/yandex-test-data.properties")
public interface YandexTestData extends Config {

    /**
     * Префикс для генерации уникальных имен тестовых папок.
     */
    @Key("test.folder.prefix")
    String testFolderPrefix();

    /**
     * Код ошибки API при попытке создать ресурс по уже существующему пути.
     */
    @Key("error.existent.directory")
    String errorExistentDirectory();

    /**
     * Код ошибки API при попытке создать ресурс по несуществующему родительскому пути.
     */
    @Key("error.path.doesnt.exist")
    String errorPathDoesntExist();

    /**
     * Несуществующий родительский каталог для негативных тестов.
     */
    @Key("non.existent.parent")
    String nonExistentParent();
}
