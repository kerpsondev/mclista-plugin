package pl.mclista.plugin.notifier;

import java.time.Duration;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import pl.mclista.plugin.updater.PluginUpdater;

public class PluginStartupNotifier implements InformationNotifier {

  private final Audience console;
  private final Duration enableTime;
  private final MiniMessage miniMessage;
  private final PluginUpdater pluginUpdater;

  public PluginStartupNotifier(
      Audience console,
      Duration enableTime,
      MiniMessage miniMessage,
      PluginUpdater pluginUpdater
  ) {
    this.console = console;
    this.enableTime = enableTime;
    this.miniMessage = miniMessage;
    this.pluginUpdater = pluginUpdater;
  }

  @Override
  public void sendNotification() {
    Stream.of(
        "",
        "            &#FFDA33&lMcLista",
        "",
        String.format("   &8» &7Plugin uruchomił się w &#F8FF00%sms.", this.enableTime.toMillis()),
        "   &8» &7Autor: &#F8FF00kerpson",
        String.format("   &8» &7Wersja: &#F8FF00%s &8(&aNajnowsza wersja: %s&8)", this.pluginUpdater.getCurrentVersion(), this.pluginUpdater.getNewestVersion()),
        "   &8» &7Strona: &#F8FF00https://www.mclista.pl",
        ""
    ).forEach(message -> this.console.sendMessage(this.miniMessage.deserialize(message)));
  }
}
