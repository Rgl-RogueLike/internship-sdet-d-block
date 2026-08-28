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
    public static class Title {
        private String raw;
        private String rendered;
    }

    @Data
    public static class Content {
        private String raw;
        private String rendered;
    }
}
