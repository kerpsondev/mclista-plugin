package pl.mclista.core.command.execute;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.core.configuration.ConfigurationFactory;
import pl.mclista.core.configuration.section.MessageConfiguration;
import pl.mclista.core.configuration.section.PluginConfiguration;
import pl.mclista.core.updater.PluginUpdater;
import pl.mclista.core.util.Placeholders;

@Command(name = "mclista-admin")
@Permission("mclista.cmd.admin")
public class AdminCommand  {

  private final MiniMessage miniMessage;
  private final PluginUpdater pluginUpdater;
  private final PluginConfiguration pluginConfiguration;
  private final ConfigurationFactory configurationFactory;
  private final MessageConfiguration messageConfiguration;

  public AdminCommand(
      @NotNull MiniMessage miniMessage,
      @NotNull PluginUpdater pluginUpdater,
      @NotNull PluginConfiguration pluginConfiguration,
      @NotNull ConfigurationFactory configurationFactory,
      @NotNull MessageConfiguration messageConfiguration
  ) {
    this.miniMessage = miniMessage;
    this.pluginUpdater = pluginUpdater;
    this.pluginConfiguration = pluginConfiguration;
    this.configurationFactory = configurationFactory;
    this.messageConfiguration = messageConfiguration;
  }

  @Execute
  void executeInformation(@Context SenderAdapter senderAdapter) {
    Placeholders placeholders = Placeholders.create()
        .withPair("{VERSION}", this.pluginUpdater.getCurrentVersion())
        .withPair("{UPDATE}", this.pluginUpdater.getNewestVersion());

    senderAdapter.sendMessage(this.miniMessage.deserialize(placeholders.apply(this.messageConfiguration.getAdminInformationMessage())));
  }

  @Execute(name = "reload")
  void executeReload(@Context SenderAdapter senderAdapter) {
    if (this.configurationFactory.reload(this.pluginConfiguration) && this.configurationFactory.reload(this.messageConfiguration)) {
      senderAdapter.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getConfigurationReloadMessage()));
      return;
    }

    senderAdapter.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getConfigurationReloadErrorMessage()));
  }
}
