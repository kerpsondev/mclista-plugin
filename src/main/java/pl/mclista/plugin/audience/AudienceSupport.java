package pl.mclista.plugin.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface AudienceSupport {

  static AudienceSupport createInstance(Plugin plugin) {
    if (Audience.class.isAssignableFrom(Player.class)) {
      return new ModernAudienceSupport(plugin);
    } else {
      return new LegacyAudienceSupport(plugin);
    }
  }

  Audience parse(Player player);

  Audience parse(CommandSender commandSender);

  Audience console();

  void closeHook();
}
