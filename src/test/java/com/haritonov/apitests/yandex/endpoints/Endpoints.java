package com.haritonov.apitests.yandex.endpoints;

/**
 * Класс-хранилище констант эндпоинтов API Яндекс.Диска.
 */
public final class Endpoints {

    private Endpoints() {
    }

    /**
     * Эндпоинт для получения информации о Диске пользователя.
     */
    public static final String DISK = "/v1/disk/";

    /**
     * Эндпоинт для управления папками
     */
    public static final String RESOURCES = "/v1/disk/resources";

    /**
     * Эндпоинт для управления корзиной
     */
    public static final String TRASH_RESOURCES = "v1/disk/trash/resources";
}
