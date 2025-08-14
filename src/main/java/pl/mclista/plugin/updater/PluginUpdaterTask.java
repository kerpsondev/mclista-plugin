package pl.mclista.plugin.updater;

import pl.mclista.plugin.notifier.InformationNotifier;

public class PluginUpdaterTask implements Runnable {

  private final PluginUpdater pluginUpdater;
  private final InformationNotifier notifier;

  public PluginUpdaterTask(PluginUpdater pluginUpdater, InformationNotifier notifier) {
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
