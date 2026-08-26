package com.haritonov.apitests.config;

import org.aeonbits.owner.ConfigFactory;

public final class ConfigManager {

    private static final Configuration CONFIG = ConfigFactory.create(Configuration.class);

    private ConfigManager() {

    }

    public static Configuration getConfig() {
        return CONFIG;
    }

}
