package com.haritonov.apitests.config;

import org.aeonbits.owner.Config;

/**
 * Интерфейс конфигурации окружения.
 * <p>
 * Связывает свойства из файла {@code config.properties} с методами Java.
 * Содержит настройки подключения к API и базе данных WordPress.
 */
@Config.Sources("classpath:configurations/config.properties")
public interface Configuration extends Config {

    /**
     * Базовый URI тестируемого стенда.
     */
    @Key("base.uri")
    String baseUri();

    /**
     * Базовый путь для REST API.
     */
    @Key("base.path")
    String basePath();

    /**
     * Имя пользователя WordPress для Basic Auth.
     */
    @Key("wp.username")
    String wpUsername();

    /**
     * Пароль пользователя WordPress.
     */
    @Key("wp.password")
    String wpPassword();

    /**
     * URL подключения к базе данных (JDBC).
     */
    @Key("db.url")
    String dbUrl();

    /**
     * Пользователь базы данных.
     */
    @Key("db.user")
    String dbUser();

    /**
     * Пароль базы данных.
     */
    @Key("db.password")
    String dbPassword();
}
