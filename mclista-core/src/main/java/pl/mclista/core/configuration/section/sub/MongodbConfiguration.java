package pl.mclista.core.configuration.section.sub;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MongodbConfiguration extends OkaeriConfig {

  @CustomKey("connection-string")
  private String connectionString = "mongodb://username:password@localhost:27017/database";

  @CustomKey("database-name")
  private String databaseName = "database";
}
