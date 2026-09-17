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

    /**
     * Код ошибки API при невалидном или пустом значении обязательного параметра.
     */
    @Key("error.field.validation")
    String errorFieldValidation();

    /**
     * Код ошибки API при запросе к несуществующему ресурсу.
     */
    @Key("error.not.found")
    String errorNotFound();

    /**
     * Префикс пути для корневого каталога Диска.
     */
    @Key("disk.path.prefix")
    String diskPrefix();

    /**
     * Суффикс, добавляемый к имени ресурса при восстановлении, если оригинальное имя уже занято
     */
    @Key("conflict.suffix")
    String conflictSuffix();

    /**
     * Имя тестового файла, используемого для загрузки на Диск.
     */
    @Key("file.name")
    String fileName();

    /**
     * Имя папки на Диске, используемой для загрузки
     */
    @Key("folder.input")
    String folderInput();

    /**
     * Имя папки на Диске, используемой для копирования
     */
    @Key("folder.output")
    String folderOutput();

    /**
     * Текстовое сообщение генерируемого текстового файла.
     */
    @Key("file.content")
    String fileContent();

    /**
     * Имя папки на Диске, используемой для загрузки и скачивания файла.
     */
    @Key("folder.sdet")
    String folderSdet();
}
