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
      "  MYSQL   - Klasyczna baza SQL, stabilna i szeroko stosowana",
      "  MARIADB - Nowoczesna, ulepszona wersja MySQL, kompatybilna i wydajna, zalecany wybór",
      "  SQLITE  - Lekka baza lokalna, nie wymaga serwera, dobry wybór dla małego serwera",
      "  MONGODB - Baza dokumentowa, elastyczna, dynamiczna"
  })
  @CustomKey("database-type")
  private DatabaseType databaseType = DatabaseType.SQLITE;

  @Comment("Konfiguracja bazy danych mysql")
  @CustomKey("mysql-configuration")
  private MysqlConfiguration mysqlConfiguration = new MysqlConfiguration();

  @Comment("Konfiguracja mongodb")
  @CustomKey("mongodb-configuration")
  private MongodbConfiguration mongodbConfiguration = new MongodbConfiguration();
}