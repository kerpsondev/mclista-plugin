package pl.mclista.bukkit.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class ModernAudienceProvider implements AudienceProvider {

  private final Server server;

  ModernAudienceProvider(@NotNull Server server) {
    this.server = server;
  }

  @Override
  public @NotNull Audience player(@NotNull Player player) {
    return player;
  }

  @Override
  public @NotNull Audience sender(@NotNull CommandSender commandSender) {
    return commandSender;
  }

  @Override
  public @NotNull Audience console() {
    return server;
  }

  @Override
  public void shutdownHook() {}
}
