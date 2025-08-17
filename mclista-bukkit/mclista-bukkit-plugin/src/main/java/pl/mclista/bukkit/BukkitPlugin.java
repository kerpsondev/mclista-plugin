package pl.mclista.bukkit;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.LibraryManager;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.DeveloperService;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.user.UserService;
import pl.mclista.bukkit.adapter.BukkitSenderAdapter;
import pl.mclista.bukkit.adapter.BukkitServerAdapter;
import pl.mclista.bukkit.adapter.transformer.BukkitPlayerTransformer;
import pl.mclista.bukkit.adapter.transformer.BukkitSenderTransformer;
import pl.mclista.bukkit.audience.AudienceProvider;
import pl.mclista.bukkit.configuration.BukkitConfigurationFactory;
import pl.mclista.bukkit.event.BukkitEventFactory;
import pl.mclista.bukkit.factory.BukkitTaskFactory;
import pl.mclista.core.PluginLoader;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.adapter.transformer.PlayerTransformer;
import pl.mclista.core.adapter.transformer.SenderTransformer;
import pl.mclista.core.command.CommandFactory;
import pl.mclista.core.configuration.ConfigurationFactory;
import pl.mclista.core.event.EventFactory;
import pl.mclista.core.runtime.RuntimeLibraries;
import pl.mclista.core.task.TaskFactory;

public class BukkitPlugin extends JavaPlugin implements DeveloperService {

  private PluginLoader pluginLoader;

  @Override
  public void onLoad() {
    LibraryManager libraryManager = new BukkitLibraryManager(this, "libraries");
    RuntimeLibraries runtimeLibraries = new RuntimeLibraries(this.getClassLoader(), libraryManager);
    runtimeLibraries.loadLibraries();
  }

  @Override
  public void onEnable() {
    AudienceProvider audienceProvider = AudienceProvider.getInstance(this);
    SenderTransformer<CommandSender> senderTransformer = new BukkitSenderTransformer(audienceProvider);
    PlayerTransformer<Player, CommandSender> playerTransformer = new BukkitPlayerTransformer(audienceProvider);
    CommandFactory<Player, CommandSender> commandFactory = new CommandFactory<>(
        LiteBukkitFactory.builder(this).settings(settings -> settings.fallbackPrefix("mclista")),
        senderTransformer,
        playerTransformer
    );

    ServerAdapter serverAdapter = new BukkitServerAdapter(this, this.getServer());
    SenderAdapter consoleAdapter = new BukkitSenderAdapter(null, audienceProvider);
    ConfigurationFactory configurationFactory = new BukkitConfigurationFactory(this.getDataFolder(), serverAdapter);
    TaskFactory taskFactory = new BukkitTaskFactory(this, this.getServer());
    EventFactory<Player> eventFactory = new BukkitEventFactory(this);

    this.pluginLoader = PluginLoader.initialize(
        taskFactory,
        eventFactory,
        serverAdapter,
        consoleAdapter,
        commandFactory,
        configurationFactory
    );

    pluginLoader.enable();

    this.getServer().getServicesManager()
        .register(
            DeveloperService.class,
            this,
            this,
            ServicePriority.Normal
        );
  }

  @Override
  public void onDisable() {
    pluginLoader.disable();
  }

  @Override
  public @NotNull UserService getUserService() {
    return pluginLoader.getUserService();
  }

  @Override
  public @NotNull RewardApiClient getRewardApiClient() {
    return pluginLoader.getRewardApiClient();
  }
}
