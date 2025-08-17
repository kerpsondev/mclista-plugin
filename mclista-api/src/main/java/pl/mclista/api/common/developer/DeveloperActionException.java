package pl.mclista.api.common.developer;

import org.jetbrains.annotations.NotNull;

public class DeveloperActionException extends RuntimeException {

  public DeveloperActionException(@NotNull String message) {
    super(message);
  }
}
