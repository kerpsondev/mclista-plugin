package pl.mclista.core.user;

import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.developer.DeveloperAction;
import pl.mclista.api.common.user.User;
import pl.mclista.api.common.user.UserService;
import pl.mclista.core.database.DatabaseConnection;
import pl.mclista.core.developer.DeveloperActionImpl;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class UserServiceImpl implements UserService {

  private final Map<UUID, User> users = new ConcurrentHashMap<>();
  private final DatabaseConnection databaseConnection;

  public UserServiceImpl(@NotNull DatabaseConnection databaseConnection) {
    this.databaseConnection = databaseConnection;
  }

  @Override
  public void addUser(@NotNull User user) {
    this.users.put(user.getUniqueId(), user);
  }

  @Override
  public void removeUser(@NotNull UUID uuid) {
    this.users.remove(uuid);
  }

  @Override
  public Optional<User> getUser(@NotNull UUID uuid) {
    return Optional.ofNullable(this.users.get(uuid));
  }

  @Override
  public @NotNull CompletableFuture<DeveloperAction<User>> loadUser(@NotNull UUID uuid) {
    User cached = this.users.get(uuid);
    if (cached != null) {
      return CompletableFuture.completedFuture(success(cached));
    }

    return databaseConnection.loadUser(uuid)
        .thenApply(user -> {
          if (user != null) {
            this.users.put(user.getUniqueId(), user);
            return success(user);
          }
          return failure((User) new UserImpl(uuid), new IllegalStateException("User not found in database"));
        })
        .exceptionally(throwable -> failure(new UserImpl(uuid), throwable));
  }

  @Override
  public @NotNull CompletableFuture<DeveloperAction<User>> modifyUser(@NotNull UUID uuid, @NotNull Consumer<User> userConsumer) {
    return loadUser(uuid).thenApply(developerAction -> {
      User user = developerAction.getResult();
      userConsumer.accept(user);
      this.databaseConnection.saveUser(user);
      return success(user);

    }).exceptionally(throwable -> failure(new UserImpl(uuid), throwable));
  }

  @Override
  public @NotNull CompletableFuture<DeveloperAction<Boolean>> saveUser(@NotNull User user) {
    return databaseConnection.saveUser(user)
        .thenApply(this::success)
        .exceptionally(throwable -> failure(false, throwable));
  }

  @Override
  public @NotNull Set<User> getUsers() {
    return Collections.unmodifiableSet(new HashSet<>(this.users.values()));
  }

  public @NotNull CompletableFuture<DeveloperAction<Set<User>>> loadUsers() {
    return databaseConnection.loadAllUsers()
        .thenApply(loaded -> {
          loaded.forEach(user -> this.users.put(user.getUniqueId(), user));
          return success((Set<User>) new HashSet<>(loaded));
        })
        .exceptionally(throwable -> failure(Collections.emptySet(), throwable));
  }

  private <T> DeveloperAction<T> success(@NotNull T result) {
    return new DeveloperActionImpl<>(result, null);
  }

  private <T> DeveloperAction<T> failure(@NotNull T result, @NotNull Throwable throwable) {
    throwable.printStackTrace();
    return new DeveloperActionImpl<>(result, throwable);
  }
}