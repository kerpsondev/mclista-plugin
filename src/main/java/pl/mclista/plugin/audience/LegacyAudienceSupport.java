package pl.mclista.plugin.audience;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LegacyAudienceSupport implements AudienceSupport {

  private final BukkitAudiences audiences;

  public LegacyAudienceSupport(Plugin plugin) {
    this.audiences = BukkitAudiences.create(plugin);
  }

  @Override
  public Audience parse(Player player) {
    return audiences.player(player);
  }

  @Override
  public Audience parse(CommandSender commandSender) {
    return audiences.sender(commandSender);
  }

  @Override
  public Audience console() {
    return audiences.console();
  }

  @Override
  public void closeHook() {
    this.audiences.close();
  }
}
