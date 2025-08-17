package pl.mclista.bukkit.event.base;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.event.base.BasePlayerLoginListener;

public class PlayerLoginListener implements Listener {

  private final BasePlayerLoginListener genericBasePlayerLoginListener;

  public PlayerLoginListener(@NotNull UserService userService) {
    this.genericBasePlayerLoginListener = new BasePlayerLoginListener(userService);
  }

  @EventHandler
  public void onLogin(PlayerLoginEvent event) {
    this.genericBasePlayerLoginListener.onLogin(event.getPlayer().getUniqueId());
  }
}
