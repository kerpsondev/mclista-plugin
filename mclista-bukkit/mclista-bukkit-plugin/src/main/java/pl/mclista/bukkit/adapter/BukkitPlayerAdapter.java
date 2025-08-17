package pl.mclista.bukkit.adapter;

import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.bukkit.audience.AudienceProvider;

public class BukkitPlayerAdapter implements PlayerAdapter<Player> {

  private final Player player;
  private final AudienceProvider audienceProvider;

  public BukkitPlayerAdapter(@NotNull Player player, @NotNull AudienceProvider audienceProvider) {
    this.player = player;
    this.audienceProvider = audienceProvider;
  }

  @Override
  public @NotNull String getName() {
    return player.getName();
  }

  @Override
  public @NotNull Player getPlayer() {
    return player;
  }

  @Override
  public @NotNull Audience getAudience() {
    return audienceProvider.player(this.player);
  }

  @Override
  public @NotNull UUID getUniqueId() {
    return player.getUniqueId();
  }
}
