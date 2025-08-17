package pl.mclista.api.common.developer;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DeveloperAction<RESULT> {

  @NotNull RESULT getResult();

  @Nullable Optional<Throwable> getThrowable();
}
