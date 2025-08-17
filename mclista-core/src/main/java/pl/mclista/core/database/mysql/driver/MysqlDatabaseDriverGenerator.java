package pl.mclista.core.database.mysql.driver;

import com.zaxxer.hikari.HikariConfig;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.configuration.section.sub.MysqlConfiguration;

class MysqlDatabaseDriverGenerator implements SqlDatabaseDriverGenerator {

  private final MysqlDrivers mysqlDrivers;
  private final MysqlConfiguration mysqlConfiguration;

  public MysqlDatabaseDriverGenerator(@NotNull MysqlDatabaseDriverGenerator.MysqlDrivers mysqlDrivers, @NotNull MysqlConfiguration mysqlConfiguration) {
    this.mysqlDrivers = mysqlDrivers;
    this.mysqlConfiguration = mysqlConfiguration;
  }

  @Override
  public @NotNull HikariConfig generateHikariConfig() {
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8",
        this.mysqlDrivers.getJdbc(),
        this.mysqlConfiguration.getHostname(),
        this.mysqlConfiguration.getPort(),
        this.mysqlConfiguration.getDatabase()
    ));

    config.setUsername(this.mysqlConfiguration.getUsername());
    config.setPassword(this.mysqlConfiguration.getPassword());
    config.setDriverClassName(this.mysqlDrivers.getDriverClass());

    config.setMaximumPoolSize(20);
    config.setMinimumIdle(5);
    config.setIdleTimeout(600000);
    config.setMaxLifetime(1800000);
    config.setConnectionTimeout(30000);
    config.setValidationTimeout(5000);
    config.setLeakDetectionThreshold(2000);

    return config;
  }

  static class MysqlDrivers {

    private final String jdbc;
    private final String driverClass;

    public MysqlDrivers(@NotNull String jdbc, @NotNull String driverClass) {
      this.jdbc = jdbc;
      this.driverClass = driverClass;
    }

    public @NotNull String getJdbc() {
      return jdbc;
    }

    public @NotNull String getDriverClass() {
      return driverClass;
    }
  }
}
