package com.haritonov.apitests.config;

import org.aeonbits.owner.Config;

/**
 * Интерфейс тестовых данных.
 * <p>
 * Связывает свойства из файла {@code test-data.properties}.
 * Содержит тестовые параметры,
 * статусы постов WordPress и коды ожидаемых ошибок API.
 */
@Config.Sources("classpath:configurations/test-data.properties")
public interface TestData extends Config {

    /**
     * Количество предложений для генерации контента поста через Faker.
     */
    @Key("faker.paragraph.size")
    int paragraphSize();

    /**
     * Статус поста "Черновик".
     */
    @Key("wp.status.draft")
    String statusDraft();

    /**
     * Статус поста "Опубликован".
     */
    @Key("wp.status.publish")
    String statusPublish();

    /**
     * Статус поста "Корзина".
     */
    @Key("wp.status.trash")
    String statusTrash();

    /**
     * Код ошибки API при передаче невалидного параметра.
     */
    @Key("wp.error.invalid.param")
    String errorInvalidParam();

    /**
     * Код ошибки API при обращении к несуществующему ID поста.
     */
    @Key("wp.error.invalid.id")
    String errorPostInvalidId();

    /**
     * Несуществующий статус поста.
     */
    @Key("wp.status.invalid")
    String statusInvalid();

    /**
     * Префикс slug для тестовых постов WordPress.
     */
    @Key("wp.slug.prefix")
    String slugPrefix();

    /**
     * Контекст запроса к API, предоставляющий доступ к raw полям
     */
    @Key("wp.context.edit")
    String contextEdit();
}
