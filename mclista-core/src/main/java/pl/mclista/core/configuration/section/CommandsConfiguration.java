package pl.mclista.core.configuration.section;

import eu.okaeri.configs.OkaeriConfig;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.util.MapUtil;
import pl.mclista.core.util.MapUtil.Pair;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommandsConfiguration extends OkaeriConfig {

  private Map<String, CommandConfiguration> commands = MapUtil.createMap(
      new Pair<>("mclista", new CommandConfiguration(true, "mclista", "nagroda", "Komenda do odebrania nagrody", "mclista.cmd.player")),
      new Pair<>("mclista-admin", new CommandConfiguration(true, "mclista-admin", "mclistaa", "Komenda administracyjna", "mclista.cmd.admin"))
  );

  public Optional<CommandConfiguration> getCommandFromName(@NotNull String name) {
    return Optional.ofNullable(this.commands.get(name));
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class CommandConfiguration extends OkaeriConfig {

    private boolean enable;
    private String name;
    private String aliases;
    private String description;
    private String permission;

    CommandConfiguration(
        boolean enable,
        @NotNull String name,
        @NotNull String aliases,
        @NotNull String description,
        @NotNull String permission
    ) {
      this.enable = enable;
      this.name = name;
      this.aliases = aliases;
      this.description = description;
      this.permission = permission;
    }
  }
}
