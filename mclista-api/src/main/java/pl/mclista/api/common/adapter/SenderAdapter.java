package pl.mclista.api.common.adapter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface SenderAdapter {

  @NotNull String getName();

  @NotNull Audience getAudience();

  default void sendMessage(@NotNull Component component) {
    getAudience().sendMessage(component);
  }
}
