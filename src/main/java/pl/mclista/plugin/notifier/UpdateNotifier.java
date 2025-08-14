package pl.mclista.plugin.notifier;

import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import pl.mclista.plugin.updater.PluginUpdater;

public class UpdateNotifier implements InformationNotifier {

  private final Audience console;
  private final MiniMessage miniMessage;
  private final PluginUpdater pluginUpdater;

  public UpdateNotifier(
      Audience console,
      MiniMessage miniMessage,
      PluginUpdater pluginUpdater
  ) {
    this.console = console;
    this.miniMessage = miniMessage;
    this.pluginUpdater = pluginUpdater;
  }

  @Override
  public void sendNotification() {
    Stream.of(
        "",
        "                 &#FFDA33&lMcLista",
        "",
        "           &cDostępna jest nowa wersja!",
        String.format("   &8» &7Najnowsza wersja: &#F8FF00%s &8(&cAktualna: %s&8)", this.pluginUpdater.getNewestVersion(), this.pluginUpdater.getCurrentVersion()),
        "   &8» &7&7Pobierz ją ze strony: &fhttps://github.com/kerpsondev/mclista-plugin/releases/latest",
        ""
    ).forEach(message -> this.console.sendMessage(this.miniMessage.deserialize(message)));
  }
}
