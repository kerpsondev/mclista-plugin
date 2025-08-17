package pl.mclista.bukkit.factory;

import java.time.Duration;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.notifier.InformationNotifier;
import pl.mclista.core.task.TaskFactory;
import pl.mclista.core.updater.PluginUpdater;
import pl.mclista.core.updater.PluginUpdaterTask;

public class BukkitTaskFactory implements TaskFactory {

  private final Plugin plugin;
  private final Server server;

  public BukkitTaskFactory(@NotNull Plugin plugin, @NotNull Server server) {
    this.plugin = plugin;
    this.server = server;
  }

  @Override
  public void runTask(@NotNull Runnable runnable) {
    BukkitScheduler scheduler = this.server.getScheduler();
    scheduler.runTask(this.plugin, runnable);
  }

  @Override
  public void runTaskAsync(@NotNull Runnable runnable) {
    BukkitScheduler scheduler = this.server.getScheduler();
    scheduler.runTaskAsynchronously(this.plugin, runnable);
  }

  @Override
  public void runTaskTimer(@NotNull Runnable runnable, @NotNull Duration delay) {
    BukkitScheduler scheduler = this.server.getScheduler();
    scheduler.runTaskTimer(this.plugin, runnable, 0L, delay.getSeconds() * 20L);
  }

  @Override
  public void runTaskTimerAsync(@NotNull Runnable runnable, @NotNull Duration delay) {
    BukkitScheduler scheduler = this.server.getScheduler();
    scheduler.runTaskTimerAsynchronously(this.plugin, runnable, 0L, delay.getSeconds() * 20L);
  }

  @Override
  public void registerTasks(@NotNull PluginUpdater pluginUpdater, @NotNull InformationNotifier pluginUpdateNotifier) {
    this.runTaskTimerAsync(new PluginUpdaterTask(pluginUpdater, pluginUpdateNotifier), Duration.ofMinutes(15L));
  }

  @Override
  public void unregister() {
    this.server.getScheduler().cancelTasks(this.plugin);
  }
}
