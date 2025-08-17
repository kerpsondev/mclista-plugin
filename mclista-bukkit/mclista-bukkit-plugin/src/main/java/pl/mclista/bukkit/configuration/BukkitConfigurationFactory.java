package pl.mclista.bukkit.configuration;

import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.configuration.ConfigurationFactory;

public class BukkitConfigurationFactory extends ConfigurationFactory {

  public BukkitConfigurationFactory(@NotNull File dataPath, @NotNull ServerAdapter serverAdapter) {
    super(dataPath, new YamlBukkitConfigurer(), serverAdapter);
  }
}
