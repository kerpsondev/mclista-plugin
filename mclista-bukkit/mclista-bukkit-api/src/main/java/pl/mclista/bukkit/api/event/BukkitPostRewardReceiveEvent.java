package pl.mclista.bukkit.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.api.common.event.ApiEvent;
import pl.mclista.api.common.user.User;

public class BukkitPostRewardReceiveEvent extends Event implements ApiEvent<Player> {

  private final static HandlerList HANDLER_LIST = new HandlerList();

  private final User user;
  private final PlayerAdapter<Player> playerAdapter;

  public BukkitPostRewardReceiveEvent(@NotNull User user, @NotNull PlayerAdapter<Player> playerAdapter) {
    this.user = user;
    this.playerAdapter = playerAdapter;
  }

  @Override
  public @NotNull Player getPlayer() {
    return playerAdapter.getPlayer();
  }

  @Override
  public @NotNull User getUser() {
    return user;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
