package com.haritonov.apitests.yandex.dto.response;

import lombok.Data;

/**
 * DTO представляющий ответ API, содержащий ссылку на ресурс
 */
@Data
public class LinkResponse {
    private String href;
    private String method;
    private boolean templated;
}
