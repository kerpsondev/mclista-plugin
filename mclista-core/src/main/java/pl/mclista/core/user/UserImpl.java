package pl.mclista.core.user;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.User;
import pl.mclista.core.util.TimeUtil;

public class UserImpl implements User {

  private final UUID uuid;
  private Instant delay;

  public UserImpl(@NotNull UUID uuid) {
    this.uuid = uuid;
    this.delay = Instant.now();
  }

  @Override
  public @NotNull UUID getUniqueId() {
    return uuid;
  }

  @Override
  public @NotNull Instant getDelay() {
    return delay;
  }

  @Override
  public boolean hasDelay() {
    return Instant.now().isBefore(this.delay);
  }

  @Override
  public void setDelay(@NotNull Instant delay) {
    this.delay = delay;
  }

  @Override
  public void updateDelay() {
    ZoneId zone = TimeUtil.ZONE_ID;
    LocalDateTime midnight = LocalDate.now(zone).plusDays(1).atStartOfDay();
    this.delay = midnight.atZone(zone).toInstant();
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    UserImpl user = (UserImpl) object;
    return uuid.equals(user.uuid);
  }
}
