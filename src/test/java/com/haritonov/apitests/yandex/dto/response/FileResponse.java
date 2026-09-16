package com.haritonov.apitests.yandex.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO представляющий ответ от API при успешном копировании файла.
 * Содержит информацию о скопированном файле.
 */
@Data
public class FileResponse {
    private String name;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("media_type")
    private String mediaType;
}
