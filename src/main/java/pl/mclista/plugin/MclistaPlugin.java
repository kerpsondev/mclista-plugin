package pl.mclista.plugin;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mclista.plugin.audience.AudienceSupport;
import pl.mclista.plugin.command.AdminCommand;
import pl.mclista.plugin.command.RewardCommand;
import pl.mclista.plugin.configuration.MessageConfiguration;
import pl.mclista.plugin.configuration.PluginConfiguration;
import pl.mclista.plugin.database.DatabaseAccessor;
import pl.mclista.plugin.http.HttpAccessor;
import pl.mclista.plugin.listener.PlayerLoginListener;
import pl.mclista.plugin.listener.PlayerQuitListener;
import pl.mclista.plugin.notifier.InformationNotifier;
import pl.mclista.plugin.notifier.PluginStartupNotifier;
import pl.mclista.plugin.notifier.UpdateNotifier;
import pl.mclista.plugin.reward.RewardApiService;
import pl.mclista.plugin.runtime.RuntimeLibraries;
import pl.mclista.plugin.updater.PluginUpdater;
import pl.mclista.plugin.updater.PluginUpdaterTask;
import pl.mclista.plugin.user.UserService;
import pl.mclista.plugin.util.CommandUtil;
import pl.mclista.plugin.util.LegacyColorProcessor;

public class MclistaPlugin extends JavaPlugin {

  private final File configFile = new File(this.getDataFolder(), "configuration.yml");
  private final File messageFile = new File(this.getDataFolder(), "messages.yml");

  private final MiniMessage miniMessage = MiniMessage.builder()
      .postProcessor(new LegacyColorProcessor())
      .build();

  private Command playerCommand;
  private Command adminCommand;

  private UserService userService;
  private PluginUpdater pluginUpdater;
  private RewardApiService rewardApiService;
  private UpdateNotifier updateNotifier;
  private AudienceSupport audienceSupport;
  private DatabaseAccessor databaseAccessor;
  private PluginConfiguration pluginConfiguration;
  private MessageConfiguration messageConfiguration;

  @Override
  public void onLoad() {
    new RuntimeLibraries(this);
  }

  @Override
  public void onEnable() {
    Instant startTime = Instant.now();

    HttpAccessor httpAccessor = new HttpAccessor();

    this.userService = new UserService();
    this.pluginUpdater = new PluginUpdater(httpAccessor, this.getDescription());
    this.audienceSupport = AudienceSupport.createInstance(this);

    this.registerConfiguration();
    this.rewardApiService = new RewardApiService(httpAccessor, this.pluginConfiguration);
    this.databaseAccessor = DatabaseAccessor.instanceFromType(this, this.pluginConfiguration.getDatabaseConfiguration());
    this.updateNotifier = new UpdateNotifier(
        this.audienceSupport.console(),
        this.miniMessage,
        this.pluginUpdater
    );

    this.registerCommand();
    this.registerListeners();
    this.registerTasks();

    InformationNotifier startupNotifier = new PluginStartupNotifier(
        this.audienceSupport.console(),
        Duration.between(startTime, Instant.now()),
        this.miniMessage,
        this.pluginUpdater
    );

    startupNotifier.sendNotification();
  }

  @Override
  public void onDisable() {
    this.databaseAccessor.saveAllUsers(this.userService.getUsers());

    CommandUtil.getCommandMap().ifPresentOrElse(commandMap -> {
      this.playerCommand.unregister(commandMap);
      this.adminCommand.unregister(commandMap);
    }, () -> {
      this.getLogger().warning("CommandMap not found!");
    });

    HandlerList.unregisterAll(this);
    this.getServer().getScheduler().cancelTasks(this);
  }

  private void registerCommand() {
    CommandUtil.getCommandMap().ifPresentOrElse(commandMap -> {
      String command = this.pluginConfiguration.getCommandName();

      this.playerCommand = new RewardCommand(
          command,
          this,
          this.getServer(),
          this.miniMessage,
          this.userService,
          this.rewardApiService,
          this.audienceSupport,
          this.pluginConfiguration,
          this.messageConfiguration
      );

      this.adminCommand = new AdminCommand(
          this.miniMessage,
          this.pluginUpdater,
          this.audienceSupport,
          this.pluginConfiguration,
          this.messageConfiguration
      );

      commandMap.register(command, this.playerCommand);
      commandMap.register(this.adminCommand.getName(), this.adminCommand);
    }, () -> {
      this.getLogger().warning("CommandMap not found!");
    });
  }

  private void registerListeners() {
    PluginManager pluginManager = this.getServer().getPluginManager();
    pluginManager.registerEvents(new PlayerLoginListener(
        this.userService,
        this.databaseAccessor
    ), this);

    pluginManager.registerEvents(new PlayerQuitListener(
        this.userService,
        this.databaseAccessor
    ), this);
  }

  private void registerTasks() {
    this.getServer().getScheduler().runTaskTimer(
        this,
        new PluginUpdaterTask(this.pluginUpdater, this.updateNotifier),
        0L,
        20L * 60 * 30
    );
  }

  private void registerConfiguration() {
    try {
      this.pluginConfiguration = ConfigManager.create(PluginConfiguration.class, (it) -> {
        it.withBindFile(this.configFile);
        it.withConfigurer(new YamlBukkitConfigurer(), new SerdesCommons());
        it.withRemoveOrphans(true);
        it.saveDefaults();
        it.load(true);
      });

      this.messageConfiguration = ConfigManager.create(MessageConfiguration.class, (it) -> {
        it.withBindFile(this.messageFile);
        it.withConfigurer(new YamlBukkitConfigurer(), new SerdesCommons());
        it.withRemoveOrphans(true);
        it.saveDefaults();
        it.load(true);
      });
    } catch (OkaeriException exception) {
      exception.printStackTrace();
      this.getServer().getPluginManager().disablePlugin(this);
    }
  }
}
