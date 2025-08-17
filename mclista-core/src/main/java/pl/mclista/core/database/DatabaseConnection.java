package pl.mclista.core.database;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.User;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.configuration.section.sub.DatabaseConfiguration;
import pl.mclista.core.database.mongodb.MongoDatabaseConnection;
import pl.mclista.core.database.mysql.MysqlDatabaseConnection;
import pl.mclista.core.database.mysql.driver.SqlDatabaseDriverGenerator;
import pl.mclista.core.database.sqlite.SqliteDatabaseConnection;

public interface DatabaseConnection {

  static DatabaseConnection instanceFromType(@NotNull ServerAdapter serverAdapter, @NotNull DatabaseConfiguration databaseConfiguration) {
    switch (databaseConfiguration.getDatabaseType()) {
      case MYSQL:
        SqlDatabaseDriverGenerator mysqlDatabaseDriverGenerator = SqlDatabaseDriverGenerator.mysql(databaseConfiguration.getMysqlConfiguration());
        return new MysqlDatabaseConnection(mysqlDatabaseDriverGenerator);
      case MARIADB:
        SqlDatabaseDriverGenerator mariadbDatabaseConfig = SqlDatabaseDriverGenerator.mariadb(databaseConfiguration.getMysqlConfiguration());
        return new MysqlDatabaseConnection(mariadbDatabaseConfig);
      case MONGODB:
        return new MongoDatabaseConnection(databaseConfiguration.getMongodbConfiguration());
      case SQLITE:
      default:
        SqlDatabaseDriverGenerator sqliteDatabaseConfig = SqlDatabaseDriverGenerator.sqlite(serverAdapter);
        return new SqliteDatabaseConnection(sqliteDatabaseConfig);
    }
  }

  CompletableFuture<User> loadUser(@NotNull UUID uuid);

  CompletableFuture<Set<User>> loadAllUsers();

  CompletableFuture<Boolean> saveUser(@NotNull User user);

  CompletableFuture<Boolean> saveAllUsers(@NotNull Collection<User> users);

  void shutdown();
}
