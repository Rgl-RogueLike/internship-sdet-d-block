package com.haritonov.apitests.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configurations/test-data.properties")
public interface TestData extends Config {

    @Key("faker.paragraph.size")
    int paragraphSize();

    @Key("wp.status.draft")
    String statusDraft();

    @Key("wp.status.publish")
    String statusPublish();

    @Key("wp.status.trash")
    String statusTrash();

    @Key("wp.error.invalid.param")
    String errorInvalidParam();
}
