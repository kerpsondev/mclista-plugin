package pl.mclista.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import pl.mclista.plugin.database.DatabaseAccessor;
import pl.mclista.plugin.user.UserService;

public class PlayerLoginListener implements Listener {

  private final UserService userService;
  private final DatabaseAccessor databaseAccessor;

  public PlayerLoginListener(UserService userService, DatabaseAccessor databaseAccessor) {
    this.userService = userService;
    this.databaseAccessor = databaseAccessor;
  }

  @EventHandler
  public void onLogin(PlayerLoginEvent event) {
    this.databaseAccessor.loadUser(event.getPlayer().getUniqueId()).thenAccept(this.userService::addUser);
  }
}
