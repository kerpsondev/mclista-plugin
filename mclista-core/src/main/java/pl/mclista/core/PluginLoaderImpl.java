package pl.mclista.core;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.client.RewardApiClientImpl;
import pl.mclista.core.command.CommandFactory;
import pl.mclista.core.configuration.ConfigurationFactory;
import pl.mclista.core.configuration.section.CommandsConfiguration;
import pl.mclista.core.configuration.section.MessageConfiguration;
import pl.mclista.core.configuration.section.PluginConfiguration;
import pl.mclista.core.database.DatabaseConnection;
import pl.mclista.core.event.EventFactory;
import pl.mclista.core.notifier.InformationNotifier;
import pl.mclista.core.notifier.PluginStartupNotifier;
import pl.mclista.core.notifier.PluginUpdateNotifier;
import pl.mclista.core.task.TaskFactory;
import pl.mclista.core.updater.PluginUpdater;
import pl.mclista.core.user.UserServiceImpl;
import pl.mclista.core.util.LegacyColorProcessor;

class PluginLoaderImpl implements PluginLoader {

  private final MiniMessage miniMessage = MiniMessage.builder()
      .postProcessor(new LegacyColorProcessor())
      .build();

  private final TaskFactory taskFactory;
  private final EventFactory eventFactory;
  private final ServerAdapter serverAdapter;
  private final SenderAdapter consoleAdapter;
  private final CommandFactory commandFactory;
  private final ConfigurationFactory configurationFactory;

  private UserService userService;
  private RewardApiClient rewardApiClient;
  private DatabaseConnection databaseConnection;

  PluginLoaderImpl(
      @NotNull TaskFactory taskFactory,
      @NotNull EventFactory eventFactory,
      @NotNull ServerAdapter serverAdapter,
      @NotNull SenderAdapter consoleAdapter,
      @NotNull CommandFactory commandFactory,
      @NotNull ConfigurationFactory configurationFactory
  ) {
    this.taskFactory = taskFactory;
    this.eventFactory = eventFactory;
    this.serverAdapter = serverAdapter;
    this.consoleAdapter = consoleAdapter;
    this.commandFactory = commandFactory;
    this.configurationFactory = configurationFactory;
  }

  @Override
  public void enable() {
    Instant startEnableTime = Instant.now();

    PluginUpdater pluginUpdater = new PluginUpdater();
    PluginConfiguration pluginConfiguration = this.configurationFactory.produce(
        PluginConfiguration.class,
        "configuration.yml"
    );

    MessageConfiguration messageConfiguration = this.configurationFactory.produce(
        MessageConfiguration.class,
        "messages.yml"
    );

    CommandsConfiguration commandsConfiguration = this.configurationFactory.produce(
        CommandsConfiguration.class,
        "commands.yml"
    );

    this.databaseConnection = DatabaseConnection.instanceFromType(this.serverAdapter, pluginConfiguration.getDatabaseConfiguration());
    this.userService = new UserServiceImpl(this.databaseConnection);
    this.rewardApiClient = new RewardApiClientImpl(pluginConfiguration);

    InformationNotifier updateNotifier = new PluginUpdateNotifier(
        this.consoleAdapter.getAudience(),
        this.miniMessage,
        pluginUpdater
    );

    this.taskFactory.registerTasks(pluginUpdater, updateNotifier);

    this.eventFactory.registerListeners(userService);
    this.commandFactory.registerCommands(
        this.taskFactory,
        this.miniMessage,
        this.userService,
        pluginUpdater,
        this.serverAdapter,
        this.rewardApiClient,
        this.eventFactory,
        pluginConfiguration,
        configurationFactory,
        messageConfiguration,
        commandsConfiguration
    );

    InformationNotifier startupNotifier = new PluginStartupNotifier(
        this.consoleAdapter.getAudience(),
        Duration.between(startEnableTime, Instant.now()),
        this.miniMessage,
        pluginUpdater
    );

    startupNotifier.sendNotification();
  }

  @Override
  public void disable() {
    Optional.ofNullable(this.taskFactory).ifPresent(TaskFactory::unregister);
    Optional.ofNullable(this.eventFactory).ifPresent(EventFactory::unregister);
    Optional.ofNullable(this.commandFactory).ifPresent(CommandFactory::unregister);

    this.databaseConnection.saveAllUsers(this.userService.getUsers());
  }

  @Override
  public @NotNull UserService getUserService() {
    return userService;
  }

  @Override
  public @NotNull RewardApiClient getRewardApiClient() {
    return rewardApiClient;
  }
}
