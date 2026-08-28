package com.haritonov.apitests.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configurations/test-data.properties")
public interface TestData extends Config {

    @Key("faker.paragraph.size")
    int paragraphSize();
}
