package pl.mclista.core.command.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.core.adapter.transformer.SenderTransformer;

public class SenderAdapterContext<SENDER> implements ContextProvider<SENDER, SenderAdapter> {

  private final SenderTransformer<SENDER> senderTransformer;

  public SenderAdapterContext(@NotNull SenderTransformer<SENDER> senderTransformer) {
    this.senderTransformer = senderTransformer;
  }

  @Override
  public ContextResult<SenderAdapter> provide(@NotNull Invocation<SENDER> invocation) {
    return ContextResult.ok(() -> senderTransformer.apply(invocation.sender()));
  }
}
