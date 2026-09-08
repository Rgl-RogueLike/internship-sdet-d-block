package com.haritonov.apitests.yandex.config;

import org.aeonbits.owner.Config;

/**
 * Интерфейс конфигурации окружения для API Яндекс.Диска.
 * <p>
 * Связывает свойства из файла {@code yandex.properties}.
 * Содержит базовый URI и OAuth-токен для авторизации запросов.
 */
@Config.Sources("classpath:configurations/yandex.properties")
public interface YandexConfig extends Config {

    /**
     * Базовый URI для API Яндекс.Диска.
     */
    @Key("yandex.base.uri")
    String baseUri();

    /**
     * OAuth-токен пользователя для авторизации.
     */
    @Key("yandex.token")
    String token();
}
