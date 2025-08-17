package pl.mclista.api.common.user;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.developer.DeveloperAction;

public interface UserService {

  void addUser(@NotNull User user);

  void removeUser(@NotNull UUID uuid);

  Optional<User> getUser(@NotNull UUID uuid);

  @NotNull CompletableFuture<DeveloperAction<User>> loadUser(@NotNull UUID uuid);

  @NotNull CompletableFuture<DeveloperAction<User>> modifyUser(@NotNull UUID uuid, @NotNull Consumer<User> userConsumer);

  @NotNull CompletableFuture<DeveloperAction<Boolean>> saveUser(@NotNull User user);

  @NotNull Set<User> getUsers();

  @NotNull CompletableFuture<DeveloperAction<Set<User>>> loadUsers();
}
