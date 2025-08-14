package pl.mclista.plugin.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserService {

  private final Map<UUID, User> users = new HashMap<>();

  public Optional<User> getUser(UUID uuid) {
    return Optional.ofNullable(this.users.get(uuid));
  }

  public Collection<User> getUsers() {
    return new HashSet<>(this.users.values());
  }

  public void addUser(User user) {
    this.users.put(user.getUuid(), user);
  }

  public void removeUser(User user) {
    this.users.remove(user.getUuid());
  }
}
