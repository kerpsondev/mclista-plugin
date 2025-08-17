package pl.mclista.core.configuration.section;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.mclista.core.configuration.section.sub.DatabaseConfiguration;

@Data
@EqualsAndHashCode(callSuper = false)
public class PluginConfiguration extends OkaeriConfig {

  @Comment("ID serwera")
  @CustomKey("server-id")
  private UUID serverId = UUID.randomUUID();

  @Comment({
      "Nagrody po użyciu komendy",
      "Zmienna {PLAYER} odnosi się do gracza otrzymującego nagrodę"
  })
  private List<String> rewards = Collections.singletonList(
      "lp user {PLAYER} parent set vip"
  );

  @Comment("Konfiguracja bazy danych")
  @CustomKey("database-configuration")
  private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
}
