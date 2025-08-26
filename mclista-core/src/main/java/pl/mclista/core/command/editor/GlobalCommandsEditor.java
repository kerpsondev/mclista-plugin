package pl.mclista.core.command.editor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.PermissionSet;
import java.util.Collections;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.configuration.section.CommandsConfiguration;
import pl.mclista.core.configuration.section.CommandsConfiguration.CommandConfiguration;

public class GlobalCommandsEditor<SENDER> implements Editor<SENDER> {

  private final CommandsConfiguration commandsConfiguration;

  public GlobalCommandsEditor(@NotNull CommandsConfiguration commandsConfiguration) {
    this.commandsConfiguration = commandsConfiguration;
  }

  @Override
  public CommandBuilder<SENDER> edit(CommandBuilder<SENDER> context) {
    Optional<CommandConfiguration> commandConfigurationOptional = this.commandsConfiguration.getCommandFromName(context.name());
    if (!commandConfigurationOptional.isPresent()) {
      return context;
    }

    CommandConfiguration commandConfiguration = commandConfigurationOptional.get();
    context.name(commandConfiguration.getName());
    context.aliases(commandConfiguration.getAliases());
    context.meta().put(Meta.DESCRIPTION, Collections.singletonList(commandConfiguration.getDescription()));

    if (commandConfiguration.getPermission() != null && !commandConfiguration.getPermission().isEmpty()) {
      context.meta().put(Meta.PERMISSIONS, Collections.singletonList(new PermissionSet(commandConfiguration.getPermission())));
    } else {
      context.meta().put(Meta.PERMISSIONS, Collections.emptyList());
    }

    if (!commandConfiguration.isEnable()) {
      context.disable();
    }

    return context;
  }
}
