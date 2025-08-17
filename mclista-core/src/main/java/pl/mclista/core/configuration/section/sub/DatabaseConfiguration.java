package pl.mclista.core.configuration.section.sub;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.mclista.core.configuration.type.DatabaseType;

@Data
@EqualsAndHashCode(callSuper = false)
public class DatabaseConfiguration extends OkaeriConfig {

  @Comment({
      "Typy bazy danych",
      "  MYSQL - Baza danych typu sql, rekomendowany wybór",
      "  MARIADB - Ulepszona wersja mysql, rekomendowany wybór",
      "  SQLITE - Baza danych lokalna, prostota użytkowania"
  })
  @CustomKey("database-type")
  private DatabaseType databaseType = DatabaseType.SQLITE;

  @Comment({
      "Konfiguracja bazy danych mysql",
      "Tylko w przypadku typu mysql lub mariadb"
  })
  @CustomKey("mysql-configuration")
  private MysqlConfiguration mysqlConfiguration = new MysqlConfiguration();
}