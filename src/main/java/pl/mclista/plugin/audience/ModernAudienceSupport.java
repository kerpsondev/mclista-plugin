package pl.mclista.plugin.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ModernAudienceSupport implements AudienceSupport {

  private final Plugin plugin;

  public ModernAudienceSupport(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public Audience parse(Player player) {
    return player;
  }

  @Override
  public Audience parse(CommandSender commandSender) {
    return commandSender;
  }

  @Override
  public Audience console() {
    return plugin.getServer();
  }

  @Override
  public void closeHook() {}
}
