package pl.mclista.plugin.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.mclista.plugin.audience.AudienceSupport;
import pl.mclista.plugin.configuration.MessageConfiguration;
import pl.mclista.plugin.configuration.PluginConfiguration;
import pl.mclista.plugin.reward.RewardApiService;
import pl.mclista.plugin.reward.RewardApiService.Result.FailureCause;
import pl.mclista.plugin.user.User;
import pl.mclista.plugin.user.UserService;
import pl.mclista.plugin.util.TimeUtil;

public class RewardCommand extends Command {

  private final Plugin plugin;
  private final Server server;
  private final MiniMessage miniMessage;
  private final UserService userService;
  private final RewardApiService rewardApiService;
  private final AudienceSupport audienceSupport;
  private final PluginConfiguration pluginConfiguration;
  private final MessageConfiguration messageConfiguration;

  public RewardCommand(
      String name,
      Plugin plugin,
      Server server,
      MiniMessage miniMessage,
      UserService userService,
      RewardApiService rewardApiService,
      AudienceSupport audienceSupport,
      PluginConfiguration pluginConfiguration,
      MessageConfiguration messageConfiguration
  ) {
    super(name);
    this.plugin = plugin;
    this.server = server;
    this.miniMessage = miniMessage;
    this.userService = userService;
    this.rewardApiService = rewardApiService;
    this.audienceSupport = audienceSupport;
    this.pluginConfiguration = pluginConfiguration;
    this.messageConfiguration = messageConfiguration;
  }

  @Override
  public boolean execute(
      @NotNull CommandSender commandSender,
      @NotNull String label,
      @NotNull String @NotNull [] strings
  ) {
    if (!(commandSender instanceof Player)) {
      Audience audience = this.audienceSupport.console();
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getCommandOnlyForPlayer()));
      return false;
    }

    Player player = (Player) commandSender;
    Audience audience = this.audienceSupport.parse(player);
    Optional<User> userOptional = this.userService.getUser(player.getUniqueId());
    if (userOptional.isEmpty()) {
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getUserNotFoundMessage()));
      return false;
    }

    User user = userOptional.get();
    if (user.hasDelay()) {
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getUserDelayMessage().replace("{TIME}", TimeUtil.format(user.getDelay()))));
      return false;
    }

    audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getDownloadInformationMessage()));

    this.rewardApiService.sendApiRequest(player).thenAccept(result -> {
      if (!result.isSuccess()) {
        Optional<FailureCause> failureCauseOptional = result.getFailureCause();
        if (failureCauseOptional.isEmpty()) {
          return;
        }

        FailureCause failureCause = failureCauseOptional.get();
        String message = this.messageConfiguration.getFailureMessages().getOrDefault(failureCause, "&cNieznany błąd");
        audience.sendMessage(this.miniMessage.deserialize(message));
        return;
      }

      this.server.getScheduler().runTask(this.plugin, () -> {
        for (String command : this.pluginConfiguration.getRewards()) {
          command = command.replace("{PLAYER}", player.getName());
          this.server.dispatchCommand(this.server.getConsoleSender(), command);
        }
      });

      user.updateDelay();
      audience.sendMessage(this.miniMessage.deserialize(this.messageConfiguration.getReceiveRewardMessage()));
    });

    return true;
  }

  @Override
  public @NotNull List<String> tabComplete(
      @NotNull CommandSender sender,
      @NotNull String alias,
      @NotNull String @NotNull [] args
  ) throws IllegalArgumentException {
    return Collections.emptyList();
  }
}
