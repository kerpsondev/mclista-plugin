package pl.mclista.plugin.listener;

import java.util.Optional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.mclista.plugin.database.DatabaseAccessor;
import pl.mclista.plugin.user.User;
import pl.mclista.plugin.user.UserService;

public class PlayerQuitListener implements Listener {

  private final UserService userService;
  private final DatabaseAccessor databaseAccessor;

  public PlayerQuitListener(UserService userService, DatabaseAccessor databaseAccessor) {
    this.userService = userService;
    this.databaseAccessor = databaseAccessor;
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Optional<User> userOptional = this.userService.getUser(event.getPlayer().getUniqueId());
    if (userOptional.isEmpty()) {
      return;
    }

    User user = userOptional.get();

    this.userService.removeUser(user);
    this.databaseAccessor.saveUser(user);
  }
}
