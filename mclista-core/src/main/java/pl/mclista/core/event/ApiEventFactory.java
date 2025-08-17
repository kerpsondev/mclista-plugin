package pl.mclista.core.event;

import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.api.common.user.User;

public interface ApiEventFactory<PLAYER> {

  boolean callPreRewardReceive(@NotNull User user, @NotNull PlayerAdapter<PLAYER> playerAdapter);

  void callPostRewardReceive(@NotNull User user, @NotNull PlayerAdapter<PLAYER> playerAdapter);
}
