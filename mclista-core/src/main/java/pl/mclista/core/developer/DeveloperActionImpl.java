package pl.mclista.core.developer;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mclista.api.common.developer.DeveloperAction;

public class DeveloperActionImpl<RESULT> implements DeveloperAction<RESULT> {

  private final RESULT result;
  private final Throwable throwable;

  public DeveloperActionImpl(@NotNull RESULT result, @Nullable Throwable throwable) {
    this.result = result;
    this.throwable = throwable;
  }

  @Override
  public @NotNull RESULT getResult() {
    return result;
  }

  @Override
  public @Nullable Optional<Throwable> getThrowable() {
    return Optional.ofNullable(this.throwable);
  }
}
