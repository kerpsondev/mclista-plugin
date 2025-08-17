package pl.mclista.core.command.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.adapter.SenderAdapter;
import pl.mclista.core.adapter.transformer.SenderTransformer;
import pl.mclista.core.configuration.section.MessageConfiguration;
import pl.mclista.core.util.Placeholders;

public class CommandMissingPermissionHandler<SENDER> implements MissingPermissionsHandler<SENDER> {

  private final MiniMessage miniMessage;
  private final MessageConfiguration messageConfiguration;
  private final SenderTransformer<SENDER> senderTransformer;

  public CommandMissingPermissionHandler(
      @NotNull MiniMessage miniMessage,
      @NotNull MessageConfiguration messageConfiguration,
      @NotNull SenderTransformer<SENDER> senderTransformer
  ) {
    this.miniMessage = miniMessage;
    this.messageConfiguration = messageConfiguration;
    this.senderTransformer = senderTransformer;
  }

  @Override
  public void handle(
      @NotNull Invocation<SENDER> invocation,
      @NotNull MissingPermissions missingPermissions,
      @NotNull ResultHandlerChain<SENDER> chain
  ) {
    String permissions = missingPermissions.asJoinedText();
    SenderAdapter senderAdapter = this.senderTransformer.apply(invocation.sender());
    Placeholders placeholders = Placeholders.create()
        .withPair("{PERMISSION}", permissions);

    senderAdapter.sendMessage(this.miniMessage.deserialize(placeholders.apply(this.messageConfiguration.getPermissionNeedMessage())));
  }
}
