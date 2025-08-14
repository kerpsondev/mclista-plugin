package pl.mclista.plugin.util;

import java.lang.reflect.Field;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

public final class CommandUtil {

  private CommandUtil() {}

  public static Optional<CommandMap> getCommandMap() {
    try {
      Server server = Bukkit.getServer();
      Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

      bukkitCommandMap.setAccessible(true);
      return Optional.ofNullable((CommandMap) bukkitCommandMap.get(server));
    } catch(Exception exception) {
      exception.printStackTrace();
      return Optional.empty();
    }
  }
}
