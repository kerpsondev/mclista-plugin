package pl.mclista.plugin.database;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.Plugin;
import pl.mclista.plugin.configuration.PluginConfiguration.DatabaseConfiguration;
import pl.mclista.plugin.user.User;

public interface DatabaseAccessor {

  static DatabaseAccessor instanceFromType(Plugin plugin, DatabaseConfiguration databaseConfiguration) {
    switch (databaseConfiguration.getDatabaseType()) {
      case MYSQL:
        return new MysqlDatabaseAccessor(databaseConfiguration.getMysqlConfiguration(), "mysql", "com.mysql.cj.jdbc.Driver");
      case MARIADB:
        return new MysqlDatabaseAccessor(databaseConfiguration.getMysqlConfiguration(), "mariadb", "org.mariadb.jdbc.Driver");
      case SQLITE:
      default:
        return new SqliteDatabaseAccessor(plugin);
    }
  }

  CompletableFuture<User> loadUser(UUID uuid);

  void saveUser(User user);

  void saveAllUsers(Collection<User> users);

  void shutdown();

  enum Type {

    MYSQL,
    SQLITE,
    MARIADB,
  }
}
