package pl.mclista.plugin.configuration;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.mclista.plugin.database.DatabaseAccessor.Type;

@Data
@EqualsAndHashCode(callSuper = false)
public class PluginConfiguration extends OkaeriConfig {

  @Comment("ID serwera")
  @CustomKey("server-id")
  private UUID serverId = UUID.randomUUID();

  @Comment("Nazwa komendy")
  @CustomKey("command-name")
  private String commandName = "nagroda";

  @Comment({
      "Nagrody po użyciu komendy",
      "Zmienna {PLAYER} odnosi się do gracza otrzymującego nagrodę"
  })
  private List<String> rewards = Arrays.asList(
      "lp user {PLAYER} parent set vip",
      "op {PLAYER}"
  );

  @Comment("Konfiguracja bazy danych")
  @CustomKey("database-configuration")
  private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class DatabaseConfiguration extends OkaeriConfig {

    @Comment({
        "Typy bazy danych",
        "  SQLITE",
        "  MYSQL",
        "  MARIADB"
    })
    @CustomKey("database-type")
    private Type databaseType = Type.SQLITE;

    @Comment({
        "Konfiguracja bazy danych mysql",
        "Tylko w przypadku typu mysql lub mariadb"
    })
    @CustomKey("mysql-configuration")
    private MysqlConfiguration mysqlConfiguration = new MysqlConfiguration();
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class MysqlConfiguration extends OkaeriConfig {

    private String hostname = "localhost";
    private int port = 3306;
    private String username = "username";
    private String password = "superSecretPassword";
    private String database = "database";
  }
}
