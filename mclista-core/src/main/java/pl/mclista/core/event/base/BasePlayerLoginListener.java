package pl.mclista.core.event.base;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.UserService;

public class BasePlayerLoginListener {

  private final UserService userService;

  public BasePlayerLoginListener(@NotNull UserService userService) {
    this.userService = userService;
  }

  public void onLogin(@NotNull UUID uuid) {
    this.userService.loadUser(uuid).thenAccept(developerAction -> {
      this.userService.addUser(developerAction.getResult());
    });
  }
}
