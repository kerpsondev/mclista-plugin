package pl.mclista.core.configuration;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.Configurer;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import java.io.File;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.adapter.ServerAdapter;

public abstract class ConfigurationFactory {

  private final File dataPath;
  private final Configurer configurer;
  private final ServerAdapter serverAdapter;

  public ConfigurationFactory(
      @NotNull File dataPath,
      @NotNull Configurer configurer,
      @NotNull ServerAdapter serverAdapter
  ) {
    this.dataPath = dataPath;
    this.configurer = configurer;
    this.serverAdapter = serverAdapter;
  }

  public <T extends OkaeriConfig> T produce(
      @NotNull Class<T> clazz,
      @NotNull String fileName,
      @NotNull OkaeriSerdesPack... serdesPacks
  ) {
    try {
      return ConfigManager.create(clazz, (it) -> {
        it.withBindFile(new File(this.dataPath, fileName));
        it.withConfigurer(this.configurer, new SerdesCommons());

        Arrays.stream(serdesPacks).forEach(it::withSerdesPack);

        it.saveDefaults();
        it.load(true);
      });
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      this.serverAdapter.disablePlugin();
      return null;
    }
  }

  public <T extends OkaeriConfig> boolean reload(@NotNull T configuration) {
    try {
      configuration.load(true);
      return true;
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      this.serverAdapter.disablePlugin();
      return false;
    }
  }
}
