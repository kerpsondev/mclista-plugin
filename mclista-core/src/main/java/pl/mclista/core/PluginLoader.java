package pl.mclista.core;

import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.command.CommandFactory;
import pl.mclista.core.configuration.ConfigurationFactory;
import pl.mclista.core.event.EventFactory;
import pl.mclista.core.task.TaskFactory;

public interface PluginLoader {

  static PluginLoader initialize(
      @NotNull TaskFactory taskFactory,
      @NotNull EventFactory<?> eventFactory,
      @NotNull ServerAdapter serverAdapter,
      @NotNull SenderAdapter consoleAdapter,
      @NotNull CommandFactory<?, ?> commandFactory,
      @NotNull ConfigurationFactory configurationFactory
  ) {
    return new PluginLoaderImpl(
        taskFactory,
        eventFactory,
        serverAdapter,
        consoleAdapter,
        commandFactory,
        configurationFactory
    );
  }

  void enable();

  void disable();

  @NotNull UserService getUserService();

  @NotNull RewardApiClient getRewardApiClient();
}
