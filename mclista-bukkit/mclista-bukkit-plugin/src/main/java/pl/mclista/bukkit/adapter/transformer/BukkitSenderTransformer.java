package pl.mclista.bukkit.adapter.transformer;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.bukkit.adapter.BukkitSenderAdapter;
import pl.mclista.bukkit.audience.AudienceProvider;
import pl.mclista.core.adapter.transformer.SenderTransformer;

public class BukkitSenderTransformer implements SenderTransformer<CommandSender> {

  private final AudienceProvider audienceProvider;

  public BukkitSenderTransformer(@NotNull AudienceProvider audienceProvider) {
    this.audienceProvider = audienceProvider;
  }

  @Override
  public SenderAdapter apply(@NotNull CommandSender commandSender) {
    return new BukkitSenderAdapter(commandSender, this.audienceProvider);
  }
}
