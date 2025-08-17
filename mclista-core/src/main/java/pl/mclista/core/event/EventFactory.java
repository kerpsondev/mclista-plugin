package pl.mclista.core.event;

import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.UserService;

public interface EventFactory<PLAYER> {

  ApiEventFactory<PLAYER> getApiEventFactory();

  void registerListeners(@NotNull UserService userService);

  void unregister();
}
