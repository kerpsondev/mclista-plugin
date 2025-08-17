package pl.mclista.core.adapter;

import java.io.File;
import org.jetbrains.annotations.NotNull;

public interface ServerAdapter {

  @NotNull File getDataFolder();

  void shutdown();

  void disablePlugin();

  void dispatchCommand(@NotNull String command);
}
