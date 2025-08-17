package pl.mclista.bukkit.adapter.transformer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.bukkit.adapter.BukkitPlayerAdapter;
import pl.mclista.bukkit.audience.AudienceProvider;
import pl.mclista.core.adapter.transformer.PlayerTransformer;

public class BukkitPlayerTransformer implements PlayerTransformer<Player, CommandSender> {

  private final AudienceProvider audienceProvider;

  public BukkitPlayerTransformer(@NotNull AudienceProvider audienceProvider) {
    this.audienceProvider = audienceProvider;
  }

  @Override
  public PlayerAdapter<Player> apply(@NotNull CommandSender commandSender) {
    return new BukkitPlayerAdapter((Player) commandSender, this.audienceProvider);
  }
}
