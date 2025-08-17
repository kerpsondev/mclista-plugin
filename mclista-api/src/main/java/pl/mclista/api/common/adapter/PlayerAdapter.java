package pl.mclista.api.common.adapter;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerAdapter<PLAYER> extends SenderAdapter {

  @NotNull UUID getUniqueId();

  @NotNull PLAYER getPlayer();
}
