package pl.mclista.api.common.user;

import java.time.Instant;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface User {

  @NotNull UUID getUniqueId();

  @NotNull Instant getDelay();

  boolean hasDelay();

  void setDelay(@NotNull Instant delay);

  void updateDelay();
}
