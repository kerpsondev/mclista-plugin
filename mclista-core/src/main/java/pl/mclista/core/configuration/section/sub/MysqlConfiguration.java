package pl.mclista.core.configuration.section.sub;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MysqlConfiguration extends OkaeriConfig {

  private String hostname = "localhost";
  private int port = 3306;
  private String username = "username";
  private String password = "superSecretPassword";
  private String database = "database";
}