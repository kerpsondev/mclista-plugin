package pl.mclista.plugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.Plugin;
import pl.mclista.plugin.user.User;

class SqliteDatabaseAccessor implements DatabaseAccessor {

  private final File dataFile;
  private final HikariDataSource hikariDataSource;

  SqliteDatabaseAccessor(Plugin plugin) {
    this.dataFile = new File(plugin.getDataFolder(), "data");
    this.dataFile.mkdir();

    HikariConfig config = new HikariConfig();

    String dbPath = new File(this.dataFile, "database.sqlite").getAbsolutePath();
    config.setJdbcUrl("jdbc:sqlite:" + dbPath);
    config.setDriverClassName("org.sqlite.JDBC");
    config.setMaximumPoolSize(10);
    config.setConnectionTimeout(30000);

    this.hikariDataSource = new HikariDataSource(config);

    this.createTable();
  }

  @Override
  public CompletableFuture<User> loadUser(UUID uuid) {
    String sql = "SELECT * FROM users WHERE uuid = ?";
    return CompletableFuture.supplyAsync(() -> {
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        statement.setString(1, uuid.toString());
        ResultSet resultSet = statement.executeQuery();

        User user = new User(uuid);
        if (resultSet.next()) {
          user.setDelay(Instant.ofEpochMilli(resultSet.getLong("delay")));
        }

        return user;
      } catch (SQLException exception) {
        exception.printStackTrace();
        return new User(uuid);
      }
    });
  }

  @Override
  public void saveUser(User user) {
    String sql = "INSERT OR REPLACE INTO users (uuid, delay) VALUES (?, ?);";
    CompletableFuture.runAsync(() -> {
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        statement.setString(1, user.getUuid().toString());
        statement.setLong(2, user.getDelay().toEpochMilli());
        statement.executeUpdate();
      } catch (SQLException exception) {
        exception.printStackTrace();
      }
    });
  }

  @Override
  public void saveAllUsers(Collection<User> users) {
    users.forEach(this::saveUser);
  }

  private void createTable() {
    String sql = "CREATE TABLE IF NOT EXISTS users (" +
        "uuid TEXT PRIMARY KEY NOT NULL, " +
        "delay INTEGER NOT NULL" +
        ");";
    try (
        Connection connection = this.hikariDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.executeUpdate();
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void shutdown() {
    Optional.ofNullable(this.hikariDataSource).ifPresent(HikariDataSource::close);
  }
}
