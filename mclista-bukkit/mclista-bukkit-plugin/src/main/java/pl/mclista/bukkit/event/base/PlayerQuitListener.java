package pl.mclista.bukkit.event.base;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.event.base.BasePlayerQuitListener;

public class PlayerQuitListener implements Listener {

  private final BasePlayerQuitListener basePlayerQuitListener;

  public PlayerQuitListener(@NotNull UserService userService) {
    this.basePlayerQuitListener = new BasePlayerQuitListener(userService);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    this.basePlayerQuitListener.onQuit(event.getPlayer().getUniqueId());
  }
}
