package pl.mclista.plugin.user;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;
import pl.mclista.plugin.util.TimeUtil;

public class User {

  private final UUID uuid;
  private Instant delay;

  public User(UUID uuid) {
    this.uuid = uuid;
    this.delay = Instant.now();
  }

  public boolean hasDelay() {
    return Instant.now().isBefore(this.delay);
  }

  public UUID getUuid() {
    return uuid;
  }

  public Instant getDelay() {
    return delay;
  }

  public void setDelay(Instant delay) {
    this.delay = delay;
  }

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

    User that = (User) object;
    return Objects.equals(uuid, that.uuid) && Objects.equals(delay, that.delay);
  }
}
