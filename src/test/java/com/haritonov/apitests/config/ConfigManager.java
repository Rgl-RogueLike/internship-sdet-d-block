package com.haritonov.apitests.config;

import com.haritonov.apitests.wordpress.config.WordPressConfig;
import com.haritonov.apitests.yandex.config.YandexConfig;
import org.aeonbits.owner.ConfigFactory;


/**
 * Менеджер конфигураций.
 * <p>
 * Отвечает за инициализацию и предоставление доступа к интерфейсам конфигурации
 * ({@link WordPressConfig} и {@link TestData}) с использованием библиотеки Owner.
 * Реализован как утилитный класс с приватным конструктором.
 */
public final class ConfigManager {

    private static final WordPressConfig WP_CONFIG = ConfigFactory.create(WordPressConfig.class);
    private static final YandexConfig YANDEX_CONFIG = ConfigFactory.create(YandexConfig.class);
    private static final TestData TEST_DATA = ConfigFactory.create(TestData.class);

    private ConfigManager() {

    }

    /**
     * Возвращает экземпляр конфигурации окружения (URL, credential).
     *
     * @return объект {@link WordPressConfig}
     */
    public static WordPressConfig getWpConfig() {
        return WP_CONFIG;
    }

    /**
     * Возвращает экземпляр конфигурации окружения (URL, credential).
     *
     * @return объект {@link YandexConfig}
     */
    public static YandexConfig getYandexConfig() {
        return YANDEX_CONFIG;
    }

    /**
     * Возвращает экземпляр конфигурации тестовых данных (статусы, коды ошибок).
     *
     * @return объект {@link TestData}
     */
    public static TestData getTestData() {
        return TEST_DATA;
    }
}
