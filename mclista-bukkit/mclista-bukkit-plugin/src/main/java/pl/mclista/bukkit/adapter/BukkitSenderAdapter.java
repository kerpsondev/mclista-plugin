package pl.mclista.bukkit.adapter;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.bukkit.audience.AudienceProvider;

public class BukkitSenderAdapter implements SenderAdapter {

  private final CommandSender commandSender;
  private final AudienceProvider audienceProvider;

  public BukkitSenderAdapter(@Nullable CommandSender commandSender, @NotNull AudienceProvider audienceProvider) {
    this.commandSender = commandSender;
    this.audienceProvider = audienceProvider;
  }

  @Override
  public @NotNull String getName() {
    return "SENDER";
  }

  @Override
  public @NotNull Audience getAudience() {
    return commandSender == null ? audienceProvider.console() : audienceProvider.sender(this.commandSender);
  }
}
