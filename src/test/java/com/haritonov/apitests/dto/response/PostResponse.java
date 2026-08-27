package com.haritonov.apitests.dto.response;

import lombok.Data;

@Data
public class PostResponse {
    private int id;
    private String status;
    private String link;
    private Title title;
    private Content content;

    @Data
    private static class Title {
        private String raw;
        private String rendered;
    }

    @Data
    private static class Content {
        private String raw;
        private String rendered;
    }
}
