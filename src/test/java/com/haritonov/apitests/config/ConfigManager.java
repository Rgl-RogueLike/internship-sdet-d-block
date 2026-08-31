package com.haritonov.apitests.config;

import org.aeonbits.owner.ConfigFactory;


/**
 * Менеджер конфигураций.
 * <p>
 * Отвечает за инициализацию и предоставление доступа к интерфейсам конфигурации
 * ({@link Configuration} и {@link TestData}) с использованием библиотеки Owner.
 * Реализован как утилитный класс с приватным конструктором.
 */
public final class ConfigManager {

    private static final Configuration CONFIG = ConfigFactory.create(Configuration.class);
    private static final TestData TEST_DATA = ConfigFactory.create(TestData.class);

    private ConfigManager() {

    }

    /**
     * Возвращает экземпляр конфигурации окружения (URL, credential).
     *
     * @return объект {@link Configuration}
     */
    public static Configuration getConfig() {
        return CONFIG;
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
