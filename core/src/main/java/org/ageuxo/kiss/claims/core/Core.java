package org.ageuxo.kiss.claims.core;

import org.ageuxo.kiss.claims.core.config.ConfigManager;
import org.ageuxo.kiss.claims.core.config.CoreConfig;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Core {

    public static final Logger LOGGER = LoggerFactory.getLogger("KISS Claims - Core");

    private static @Nullable ConfigManager<CoreConfig> coreConfigManager;
    private static @Nullable CoreConfig coreConfig;

    public static void init() {
        LOGGER.info("Initialising KISS Claims...");

        coreConfigManager = new ConfigManager<>(Path.of("kiss_claims-core.toml"), CoreConfig.class, CoreConfig::new);
        try {
            coreConfigManager.load();
            coreConfig = coreConfigManager.getConfig();
            LOGGER.info("Loaded Core Config.");
            
            coreConfigManager.save();
        } catch (IOException e) {
            throw new RuntimeException("Error during loading of Core Config: " + e.getCause(), e);
        }


    }

    public static CoreConfig coreConfig() {
        return Objects.requireNonNull(coreConfig);
    }
}
