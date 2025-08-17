package pl.mclista.core.task;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.notifier.InformationNotifier;
import pl.mclista.core.updater.PluginUpdater;

public interface TaskFactory {

  void runTask(@NotNull Runnable runnable);

  void runTaskAsync(@NotNull Runnable runnable);

  void runTaskTimer(@NotNull Runnable runnable, @NotNull Duration delay);

  void runTaskTimerAsync(@NotNull Runnable runnable, @NotNull Duration delay);

  void registerTasks(@NotNull PluginUpdater pluginUpdater, @NotNull InformationNotifier pluginUpdateNotifier);

  void unregister();
}
