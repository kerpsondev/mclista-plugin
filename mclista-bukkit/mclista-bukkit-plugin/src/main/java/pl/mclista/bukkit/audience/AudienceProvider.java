package pl.mclista.bukkit.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface AudienceProvider {

  static AudienceProvider getInstance(@NotNull Plugin plugin) {
    if (Audience.class.isAssignableFrom(Player.class)) {
      return new ModernAudienceProvider(plugin.getServer());
    } else {
      return new LegacyAudienceProvider(plugin);
    }
  }

  @NotNull Audience player(@NotNull Player player);

  @NotNull Audience sender(@NotNull CommandSender commandSender);

  @NotNull Audience console();

  void shutdownHook();
}
