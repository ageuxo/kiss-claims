package org.ageuxo.kiss.claims.core.config;

import io.github.wasabithumb.jtoml.configurate.TomlConfigurationLoader;
import org.ageuxo.kiss.claims.core.Core;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;

public class ConfigManager<C> {

    private final Path path;
    private final Class<C> configClass;
    private final Supplier<C> configSupplier;
    private final TomlConfigurationLoader loader;
    private final ObjectMapper.Factory mapperFactory;

    private @Nullable C config;
    private @Nullable CommentedConfigurationNode root;

    public ConfigManager(Path path, Class<C> configClass, Supplier<C> configSupplier) {
        this.path = path;

        this.loader = TomlConfigurationLoader.builder()
                    .path(path)
                    .build();
        this.configClass = configClass;
        this.configSupplier = configSupplier;

        this.mapperFactory = ObjectMapper.factoryBuilder()
                .build();
    }

    public void load() throws IOException {
        // Ensure path exists
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        root = loader.load();

        var mapper = mapperFactory.get(configClass);

        if (root.empty()) {
            config = configSupplier.get();

            mapper.save(config, root);
            loader.save(root);

            Core.LOGGER.info("Created default config: {}", path);
            return;
        }

        config = mapper.load(root);

        Core.LOGGER.info("Loaded config: {}", path);
    }

    public void save() throws ConfigurateException {
        Objects.requireNonNull(config, "Attempted to save config '%s' before it had loaded.".formatted(path));
        Objects.requireNonNull(root, "Attempted to save config '%s' before it had loaded.".formatted(path));

        mapperFactory.get(configClass)
                .save(config, root);

        loader.save(root);

    }

    public C getConfig() {
        return Objects.requireNonNull(config, "Attempted to access config '%s' before it had loaded.".formatted(path));
    }

}
