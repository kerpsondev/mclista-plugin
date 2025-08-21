package pl.mclista.core.command.execute;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.api.common.client.ApiFailtureCause;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.client.RewardApiResponse;
import pl.mclista.api.common.user.User;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.configuration.section.MessageConfiguration;
import pl.mclista.core.configuration.section.PluginConfiguration;
import pl.mclista.core.event.EventFactory;
import pl.mclista.core.task.TaskFactory;
import pl.mclista.core.util.Placeholders;
import pl.mclista.core.util.TimeUtil;

@Command(name = "mclista")
@Permission("mclista.cmd.base")
public class BaseCommand {

  private final RewardApiClient rewardApiClient;
  private final TaskFactory taskFactory;
  private final MiniMessage miniMessage;
  private final UserService userService;
  private final ServerAdapter serverAdapter;
  private final EventFactory<?> eventFactory;
  private final PluginConfiguration pluginConfiguration;
  private final MessageConfiguration messageConfiguration;

  public BaseCommand(
      @NotNull RewardApiClient rewardApiClient,
      @NotNull TaskFactory taskFactory,
      @NotNull MiniMessage miniMessage,
      @NotNull UserService userService,
      @NotNull ServerAdapter serverAdapter,
      @NotNull EventFactory<?> eventFactory,
      @NotNull PluginConfiguration pluginConfiguration,
      @NotNull MessageConfiguration messageConfiguration
  ) {
    this.rewardApiClient = rewardApiClient;
    this.taskFactory = taskFactory;
    this.miniMessage = miniMessage;
    this.userService = userService;
    this.serverAdapter = serverAdapter;
    this.eventFactory = eventFactory;
    this.pluginConfiguration = pluginConfiguration;
    this.messageConfiguration = messageConfiguration;
  }

  @Execute
  void onCommand(@Context PlayerAdapter playerAdapter) {
    Audience audience = playerAdapter.getAudience();
    Optional<User> userOptional = this.userService.getUser(playerAdapter.getUniqueId());
    if (!userOptional.isPresent()) {
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getUserNotFoundMessage()));
      return;
    }

    User user = userOptional.get();
    if (user.hasDelay()) {
      Placeholders placeholders = Placeholders.create()
          .withPair("{TIME}", TimeUtil.format(user.getDelay()));

      audience.sendMessage(this.miniMessage.deserialize(placeholders.apply(this.messageConfiguration.getUserDelayMessage())));
      return;
    }

    if (!this.eventFactory.getApiEventFactory().callPreRewardReceive(user, playerAdapter)) {
      return;
    }

    audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getDownloadInformationMessage()));

    this.rewardApiClient.sendRequest(playerAdapter.getName()).thenAccept(developerAction -> {
      RewardApiResponse response = developerAction.getResult();
      if (!response.isSuccess()) {
        Optional<ApiFailtureCause> apiFailtureCauseOptional = response.getCause();
        if (!apiFailtureCauseOptional.isPresent()) {
          return;
        }

        ApiFailtureCause apiFailtureCause = apiFailtureCauseOptional.get();
        String message = this.messageConfiguration.getFailureMessages().getOrDefault(apiFailtureCause, "&cNieznany błąd");
        audience.sendMessage(this.miniMessage.deserialize(message));
        return;
      }

      this.taskFactory.runTask(() -> {
        Placeholders placeholders = Placeholders.create()
            .withPair("{PLAYER}", playerAdapter.getName());

        for (String command : this.pluginConfiguration.getRewards()) {
          this.serverAdapter.dispatchCommand(placeholders.apply(command));
        }

        user.updateDelay();
        this.userService.saveUser(user);

        this.eventFactory.getApiEventFactory().callPostRewardReceive(user, playerAdapter);
      });

      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getReceiveRewardMessage()));
    });
  }
}
