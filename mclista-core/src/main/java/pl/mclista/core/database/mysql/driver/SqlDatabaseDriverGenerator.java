package pl.mclista.core.database.mysql.driver;

import com.zaxxer.hikari.HikariConfig;
import org.jetbrains.annotations.NotNull;
import pl.mclista.core.adapter.ServerAdapter;
import pl.mclista.core.configuration.section.sub.MysqlConfiguration;
import pl.mclista.core.database.mysql.driver.MysqlDatabaseDriverGenerator.MysqlDrivers;

public interface SqlDatabaseDriverGenerator {

  static SqlDatabaseDriverGenerator mysql(@NotNull MysqlConfiguration mysqlConfiguration) {
    MysqlDrivers mysqlDrivers = new MysqlDrivers("mysql", "com.mysql.cj.jdbc.Driver");
    return new MysqlDatabaseDriverGenerator(mysqlDrivers, mysqlConfiguration);
  }

  static SqlDatabaseDriverGenerator mariadb(@NotNull MysqlConfiguration mysqlConfiguration) {
    MysqlDrivers mysqlDrivers = new MysqlDrivers("mariadb", "org.mariadb.jdbc.Driver");
    return new MysqlDatabaseDriverGenerator(mysqlDrivers, mysqlConfiguration);
  }

  static SqlDatabaseDriverGenerator sqlite(@NotNull ServerAdapter serverAdapter) {
    return new SqliteDatabaseDriverGenerator(serverAdapter);
  }

  @NotNull HikariConfig generateHikariConfig();
}
