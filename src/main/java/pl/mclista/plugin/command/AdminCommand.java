package pl.mclista.plugin.command;

import eu.okaeri.configs.exception.OkaeriException;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.mclista.plugin.audience.AudienceSupport;
import pl.mclista.plugin.configuration.MessageConfiguration;
import pl.mclista.plugin.configuration.PluginConfiguration;
import pl.mclista.plugin.updater.PluginUpdater;

public class AdminCommand extends Command {

  private final MiniMessage miniMessage;
  private final PluginUpdater pluginUpdater;
  private final AudienceSupport audienceSupport;
  private final PluginConfiguration pluginConfiguration;
  private final MessageConfiguration messageConfiguration;

  public AdminCommand(
      MiniMessage miniMessage,
      PluginUpdater pluginUpdater,
      AudienceSupport audienceSupport,
      PluginConfiguration pluginConfiguration,
      MessageConfiguration messageConfiguration
  ) {
    super("mclista-admin");
    this.miniMessage = miniMessage;
    this.pluginUpdater = pluginUpdater;
    this.audienceSupport = audienceSupport;
    this.pluginConfiguration = pluginConfiguration;
    this.messageConfiguration = messageConfiguration;
  }

  @Override
  public boolean execute(
      @NotNull CommandSender commandSender,
      @NotNull String s,
      @NotNull String @NotNull [] strings
  ) {
    Audience audience = this.audienceSupport.parse(commandSender);
    if (!commandSender.hasPermission("mclista.admin")) {
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getPermissionNeedMessage()));
    }

    if (strings.length == 0) {
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getAdminInformationMessage()
          .replace("{VERSION}", this.pluginUpdater.getCurrentVersion())
          .replace("{UPDATE}", this.pluginUpdater.getNewestVersion())
      ));

      return true;
    }

    try {
      this.pluginConfiguration.load();
      this.messageConfiguration.load();

      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getConfigurationReloadMessage()));
      return true;
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getConfigurationReloadErrorMessage()));
      return false;
    }
  }

  @Override
  public @NotNull List<String> tabComplete(
      @NotNull CommandSender sender,
      @NotNull String alias,
      @NotNull String @NotNull [] args
  ) throws IllegalArgumentException {
    if (args.length == 1) {
      return Collections.singletonList("reload");
    }

    return Collections.emptyList();
  }
}
