package pl.mclista.core.database.mysql;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.User;
import pl.mclista.core.database.DatabaseConnection;
import pl.mclista.core.database.mysql.driver.SqlDatabaseDriverGenerator;
import pl.mclista.core.user.UserImpl;

public class MysqlDatabaseConnection implements DatabaseConnection {

  private final HikariDataSource hikariDataSource;

  public MysqlDatabaseConnection(@NotNull SqlDatabaseDriverGenerator databaseConfig) {
    this.hikariDataSource = new HikariDataSource(databaseConfig.generateHikariConfig());
    this.createTable();
  }

  @Override
  public CompletableFuture<User> loadUser(@NotNull UUID uuid) {
    String sql = "SELECT * FROM `users` WHERE `uuid` = ?";
    return CompletableFuture.supplyAsync(() -> {
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        statement.setString(1, uuid.toString());
        ResultSet resultSet = statement.executeQuery();

        User user = new UserImpl(uuid);
        if (resultSet.next()) {
          user.setDelay(Instant.ofEpochMilli(resultSet.getLong("delay")));
        }

        return user;
      } catch (SQLException exception) {
        exception.printStackTrace();
        return new UserImpl(uuid);
      }
    });
  }

  @Override
  public CompletableFuture<Set<User>> loadAllUsers() {
    String sql = "SELECT * FROM `users`;";
    return CompletableFuture.supplyAsync(() -> {
      Set<User> users = new HashSet<>();
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
          User user = new UserImpl(UUID.fromString(resultSet.getString("uuid")));
          user.setDelay(Instant.ofEpochMilli(resultSet.getLong("delay")));
          users.add(user);
        }

        return users;
      } catch (SQLException exception) {
        exception.printStackTrace();
        return users;
      }
    });
  }

  @Override
  public CompletableFuture<Boolean> saveUser(@NotNull User user) {
    String sql = "INSERT INTO `users` (`uuid`, `delay`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `delay` = VALUES(`delay`);";
    return CompletableFuture.supplyAsync(() -> {
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        statement.setString(1, user.getUniqueId().toString());
        statement.setLong(2, user.getDelay().toEpochMilli());
        statement.executeUpdate();
        return true;
      } catch (SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  @Override
  public CompletableFuture<Boolean> saveAllUsers(@NotNull Collection<User> users) {
    String sql = "INSERT INTO `users` (`uuid`, `delay`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `delay` = VALUES(`delay`);";
    return CompletableFuture.supplyAsync(() -> {
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        for (User user : users) {
          statement.setString(1, user.getUniqueId().toString());
          statement.setLong(2, user.getDelay().toEpochMilli());
          statement.addBatch();
        }

        statement.executeBatch();
        return true;
      } catch (SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  private void createTable() {
    String sql = "CREATE TABLE IF NOT EXISTS `users` (`uuid` CHAR(36) NOT NULL PRIMARY KEY, `delay` LONG NOT NULL);";
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
