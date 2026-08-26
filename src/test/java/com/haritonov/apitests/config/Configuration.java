package com.haritonov.apitests.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configurations/config.properties")
public interface Configuration extends Config {

    @Key("base.uri")
    String baseUri();

    @Key("base.path")
    String basePath();

    @Key("wp.username")
    String wpUsername();

    @Key("wp.password")
    String wpPassword();

    @Key("db.url")
    String dbUrl();

    @Key("db.user")
    String dbUser();

    @Key("db.password")
    String dbPassword();
}
