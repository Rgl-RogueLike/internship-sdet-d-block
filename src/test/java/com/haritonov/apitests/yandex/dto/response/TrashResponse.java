package com.haritonov.apitests.yandex.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


/**
 * DTO представляющий ответ от эндпоинта корзины Яндекс.Диска.
 * <p>
 * Содержит вложенный объект {@link Embedded}, который хранит список удаленных ресурсов.
 */
@Data
public class TrashResponse {

    /**
     * Вложенный объект с данными о содержимом корзины.
     */
    @JsonProperty("_embedded")
    private Embedded embedded;

    /**
     * Вложенный класс, представляющий структуру содержимого корзины.
     */
    @Data
    public static class Embedded {

        /**
         * Список элементов (папок/файлов), находящихся в корзине.
         */
        private List<TrashItem> items;
    }
}
