package pl.mclista.core.command;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import java.util.Optional;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.adapter.transformer.PlayerTransformer;
import pl.mclista.core.adapter.transformer.SenderTransformer;
import pl.mclista.core.command.context.PlayerAdapterContext;
import pl.mclista.core.command.context.SenderAdapterContext;
import pl.mclista.core.command.editor.GlobalCommandsEditor;
import pl.mclista.core.command.execute.AdminCommand;
import pl.mclista.core.command.execute.BaseCommand;
import pl.mclista.core.command.handler.CommandMissingPermissionHandler;
import pl.mclista.core.configuration.ConfigurationFactory;
import pl.mclista.core.configuration.section.CommandsConfiguration;
import pl.mclista.core.configuration.section.MessageConfiguration;
import pl.mclista.core.configuration.section.PluginConfiguration;
import pl.mclista.core.event.EventFactory;
import pl.mclista.core.task.TaskFactory;
import pl.mclista.core.updater.PluginUpdater;

public class CommandFactory<PLAYER, SENDER> {

  private final LiteCommandsBuilder<SENDER, ?, ?> liteCommandsBuilder;
  private final SenderTransformer<SENDER> senderTransformer;
  private final PlayerTransformer<PLAYER, SENDER> playerTransformer;

  private LiteCommands<SENDER> liteCommands;

  public CommandFactory(
      @NotNull LiteCommandsBuilder<SENDER, ?, ?> liteCommandsBuilder,
      @NotNull SenderTransformer<SENDER> senderTransformer,
      @NotNull PlayerTransformer<PLAYER, SENDER> playerTransformer
  ) {
    this.liteCommandsBuilder = liteCommandsBuilder;
    this.senderTransformer = senderTransformer;
    this.playerTransformer = playerTransformer;
  }

  public void registerCommands(
      @NotNull TaskFactory taskFactory,
      @NotNull MiniMessage miniMessage,
      @NotNull UserService userService,
      @NotNull PluginUpdater pluginUpdater,
      @NotNull ServerAdapter serverAdapter,
      @NotNull RewardApiClient rewardApiClient,
      @NotNull EventFactory<PLAYER> eventFactory,
      @NotNull PluginConfiguration pluginConfiguration,
      @NotNull ConfigurationFactory configurationFactory,
      @NotNull MessageConfiguration messageConfiguration,
      @NotNull CommandsConfiguration commandsConfiguration
  ) {
    this.liteCommands = this.liteCommandsBuilder
        .commands(
            new BaseCommand(
                rewardApiClient,
                taskFactory,
                miniMessage,
                userService,
                serverAdapter,
                eventFactory,
                pluginConfiguration,
                messageConfiguration
            ),
            new AdminCommand(
                miniMessage,
                pluginUpdater,
                pluginConfiguration,
                configurationFactory,
                messageConfiguration
            )
        )
        .editorGlobal(new GlobalCommandsEditor<>(commandsConfiguration))
        .context(SenderAdapter.class, new SenderAdapterContext<>(this.senderTransformer))
        .context(PlayerAdapter.class, new PlayerAdapterContext<>(this.playerTransformer))
        .missingPermission(new CommandMissingPermissionHandler<>(miniMessage, messageConfiguration, this.senderTransformer))
        .build();
  }

  public void unregister() {
    Optional.ofNullable(this.liteCommands).ifPresent(LiteCommands::unregister);
  }
}
