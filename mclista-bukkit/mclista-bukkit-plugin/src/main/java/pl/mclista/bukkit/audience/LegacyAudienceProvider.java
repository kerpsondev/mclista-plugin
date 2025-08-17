package pl.mclista.bukkit.audience;

import java.util.Optional;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

class LegacyAudienceProvider implements AudienceProvider {

  private final BukkitAudiences audiences;

  public LegacyAudienceProvider(@NotNull Plugin plugin) {
    this.audiences = BukkitAudiences.create(plugin);
  }

  @Override
  public @NotNull Audience player(@NotNull Player player) {
    return audiences.player(player);
  }

  @Override
  public @NotNull Audience sender(@NotNull CommandSender commandSender) {
    return audiences.sender(commandSender);
  }

  @Override
  public @NotNull Audience console() {
    return audiences.console();
  }

  @Override
  public void shutdownHook() {
    Optional.ofNullable(this.audiences).ifPresent(BukkitAudiences::close);
  }
}
