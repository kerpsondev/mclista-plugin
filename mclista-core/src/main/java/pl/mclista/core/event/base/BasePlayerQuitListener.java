package pl.mclista.core.event.base;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.UserService;

public class BasePlayerQuitListener {

  private final UserService userService;

  public BasePlayerQuitListener(@NotNull UserService userService) {
    this.userService = userService;
  }

  public void onQuit(@NotNull UUID uuid) {
    this.userService.getUser(uuid).ifPresent(this.userService::saveUser);
  }
}
