package pl.mclista.api.common.event;

import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.User;

public interface ApiEvent<PLAYER> {

  @NotNull PLAYER getPlayer();

  @NotNull User getUser();
}
