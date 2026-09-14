package com.haritonov.apitests.yandex.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO представляющий элемент, находящийся в корзине Яндекс.Диска.
 * <p>
 * Содержит реальный путь ресурса в корзине (с уникальным хэшем) и его оригинальный путь до удаления.
 */
@Data
public class TrashItem {

    /**
     * Реальный путь ресурса в корзине.
     */
    private String path;


    /**
     * Оригинальный путь ресурса до удаления.
     */
    @JsonProperty("origin_path")
    private String originPath;
}
