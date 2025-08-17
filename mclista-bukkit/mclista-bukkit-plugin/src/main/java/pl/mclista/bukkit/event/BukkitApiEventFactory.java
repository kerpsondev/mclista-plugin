package pl.mclista.bukkit.event;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.api.common.user.User;
import pl.mclista.bukkit.api.event.BukkitPostRewardReceiveEvent;
import pl.mclista.bukkit.api.event.BukkitPreRewardReceiveEvent;
import pl.mclista.core.event.ApiEventFactory;

public class BukkitApiEventFactory implements ApiEventFactory<Player> {

  private final Server server;

  public BukkitApiEventFactory(@NotNull Server server) {
    this.server = server;
  }

  @Override
  public boolean callPreRewardReceive(@NotNull User user, @NotNull PlayerAdapter<Player> playerAdapter) {
    BukkitPreRewardReceiveEvent event = new BukkitPreRewardReceiveEvent(user, playerAdapter);
    this.server.getPluginManager().callEvent(event);

    return !event.isCancelled();
  }

  @Override
  public void callPostRewardReceive(@NotNull User user, @NotNull PlayerAdapter<Player> playerAdapter) {
    BukkitPostRewardReceiveEvent event = new BukkitPostRewardReceiveEvent(user, playerAdapter);
    this.server.getPluginManager().callEvent(event);
  }
}
