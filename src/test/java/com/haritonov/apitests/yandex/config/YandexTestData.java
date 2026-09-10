package com.haritonov.apitests.yandex.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configurations/yandex-test-data.properties")
public interface YandexTestData extends Config {

    @Key("test.folder.prefix")
    String testFolderPrefix();
}
