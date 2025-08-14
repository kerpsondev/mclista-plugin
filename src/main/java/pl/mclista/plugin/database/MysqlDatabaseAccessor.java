package pl.mclista.plugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import pl.mclista.plugin.configuration.PluginConfiguration.MysqlConfiguration;
import pl.mclista.plugin.user.User;

class MysqlDatabaseAccessor implements DatabaseAccessor {

  private HikariDataSource hikariDataSource;

  MysqlDatabaseAccessor(MysqlConfiguration mysqlConfiguration, String jdbc, String driver) {
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", jdbc, mysqlConfiguration.getHostname(), mysqlConfiguration.getPort(), mysqlConfiguration.getDatabase()));
    config.setUsername(mysqlConfiguration.getUsername());
    config.setPassword(mysqlConfiguration.getPassword());
    config.setDriverClassName(driver);

    config.setMaximumPoolSize(10);
    config.setMinimumIdle(5);
    config.setIdleTimeout(300000);
    config.setMaxLifetime(1800000);
    config.setConnectionTimeout(30000);
    config.setLeakDetectionThreshold(20000);

    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("useServerPrepStmts", "true");
    config.addDataSourceProperty("useLocalSessionState", "true");
    config.addDataSourceProperty("cacheResultSetMetadata", "true");
    config.addDataSourceProperty("maintainTimeStats", "false");
    config.addDataSourceProperty("alwaysSendSetIsolation", "false");
    config.addDataSourceProperty("useUnbufferedInput", "false");

    this.hikariDataSource = new HikariDataSource(config);
    
    this.createTable();
  }

  @Override
  public CompletableFuture<User> loadUser(UUID uuid) {
    String sql = "SELECT * FROM `users` WHERE `uuid` = ?";
    return CompletableFuture.supplyAsync(() -> {
      try (
          Connection connection = this.hikariDataSource.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql)
      ) {
        statement.setString(1, uuid.toString());
        ResultSet resultSet = statement.executeQuery();

        User user = new User(uuid);
        while (resultSet.next()) {
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
    String sql = "INSERT INTO `users` (`uuid`, `delay`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `delay` = VALUES(`delay`);";
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
