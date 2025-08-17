package pl.mclista.bukkit.adapter;

import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.adapter.ServerAdapter;

public class BukkitServerAdapter implements ServerAdapter {

  private final Plugin plugin;
  private final Server server;

  public BukkitServerAdapter(@NotNull Plugin plugin, @NotNull Server server) {
    this.plugin = plugin;
    this.server = server;
  }

  @Override
  public @NotNull File getDataFolder() {
    return plugin.getDataFolder();
  }

  @Override
  public void shutdown() {
    this.server.shutdown();
  }

  @Override
  public void disablePlugin() {
    this.server.getPluginManager().disablePlugin(this.plugin);
  }

  @Override
  public void dispatchCommand(@NotNull String command) {
    this.server.dispatchCommand(this.server.getConsoleSender(), command);
  }
}
