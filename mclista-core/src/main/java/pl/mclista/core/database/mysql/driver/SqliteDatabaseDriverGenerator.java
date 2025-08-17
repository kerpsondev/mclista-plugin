package pl.mclista.core.database.mysql.driver;

import com.zaxxer.hikari.HikariConfig;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.adapter.ServerAdapter;

class SqliteDatabaseDriverGenerator implements SqlDatabaseDriverGenerator {

  private final ServerAdapter serverAdapter;

  public SqliteDatabaseDriverGenerator(@NotNull ServerAdapter serverAdapter) {
    this.serverAdapter = serverAdapter;
  }

  @Override
  public @NotNull HikariConfig generateHikariConfig() {
    File dataFile = new File(this.serverAdapter.getDataFolder(), "data");
    dataFile.mkdir();

    HikariConfig config = new HikariConfig();

    String dbPath = new File(dataFile, "database.sqlite").getAbsolutePath();
    config.setJdbcUrl("jdbc:sqlite:" + dbPath);
    config.setDriverClassName("org.sqlite.JDBC");
    config.setMaximumPoolSize(20);
    config.setMinimumIdle(5);
    config.setIdleTimeout(600000);
    config.setMaxLifetime(1800000);
    config.setConnectionTimeout(30000);
    config.setValidationTimeout(5000);
    config.setLeakDetectionThreshold(2000);

    return config;
  }
}
