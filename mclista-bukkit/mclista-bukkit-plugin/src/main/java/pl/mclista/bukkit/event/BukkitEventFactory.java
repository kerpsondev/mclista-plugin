package pl.mclista.bukkit.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.UserService;
import pl.mclista.bukkit.event.base.PlayerLoginListener;
import pl.mclista.bukkit.event.base.PlayerQuitListener;
import pl.mclista.core.event.ApiEventFactory;
import pl.mclista.core.event.EventFactory;

public class BukkitEventFactory implements EventFactory<Player> {

  private final Plugin plugin;
  private final ApiEventFactory<Player> apiEventFactory;

  public BukkitEventFactory(@NotNull Plugin plugin) {
    this.plugin = plugin;
    this.apiEventFactory = new BukkitApiEventFactory(plugin.getServer());
  }

  @Override
  public ApiEventFactory<Player> getApiEventFactory() {
    return apiEventFactory;
  }

  @Override
  public void registerListeners(@NotNull UserService userService) {
    PluginManager pluginManager = this.plugin.getServer().getPluginManager();
    pluginManager.registerEvents(new PlayerQuitListener(userService), this.plugin);
    pluginManager.registerEvents(new PlayerLoginListener(userService), this.plugin);
  }

  @Override
  public void unregister() {
    HandlerList.unregisterAll(this.plugin);
  }
}
