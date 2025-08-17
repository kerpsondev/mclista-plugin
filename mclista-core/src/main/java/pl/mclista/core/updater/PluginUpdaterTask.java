package pl.mclista.core.updater;

import org.jetbrains.annotations.NotNull;
import pl.mclista.core.notifier.InformationNotifier;

public class PluginUpdaterTask implements Runnable {

  private final PluginUpdater pluginUpdater;
  private final InformationNotifier notifier;

  public PluginUpdaterTask(@NotNull PluginUpdater pluginUpdater, @NotNull InformationNotifier notifier) {
    this.pluginUpdater = pluginUpdater;
    this.notifier = notifier;
  }

  @Override
  public void run() {
    this.pluginUpdater.checkNewestVersion().thenAccept(ignored -> {
      if (!this.pluginUpdater.needUpdate()) {
        return;
      }

      this.notifier.sendNotification();
    });
  }
}
