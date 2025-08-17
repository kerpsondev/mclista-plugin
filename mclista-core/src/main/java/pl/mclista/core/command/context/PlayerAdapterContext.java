package pl.mclista.core.command.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.PlayerAdapter;
import pl.mclista.core.adapter.transformer.PlayerTransformer;

public class PlayerAdapterContext<PLAYER, SENDER> implements ContextProvider<SENDER, PlayerAdapter> {

  private final PlayerTransformer<PLAYER, SENDER> playerTransformer;

  public PlayerAdapterContext(@NotNull PlayerTransformer<PLAYER, SENDER> playerTransformer) {
    this.playerTransformer = playerTransformer;
  }

  @Override
  public ContextResult<PlayerAdapter> provide(@NotNull Invocation<SENDER> invocation) {
    return ContextResult.ok(() -> this.playerTransformer.apply(invocation.sender()));
  }
}
