package pl.mclista.core.configuration.section;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.mclista.api.common.client.ApiFailtureCause;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageConfiguration extends OkaeriConfig {

  @Comment({
      "Błędy podczas próby odbioru nagrody",
      "Rodzaje błędów:",
      "  UNKNOWN - Nieznany błąd, należy zgłosić się do administracji",
      "  INVALID_NICKNAME - Niepoprawny nick gracza",
      "  NEVER_UPVOTED - Gracz nie głosował",
      "  SERVER_NOT_FOUND - Nie znaleziono serwera",
      "  INVALID_SERVER_ID - Niepoprawne id serwera, należy zgłosić się do administracji"
  })
  @CustomKey("failure-messages")
  private Map<ApiFailtureCause, String> failureMessages = new HashMap<ApiFailtureCause, String>() {{
    this.put(ApiFailtureCause.UNKNOWN, "&cNieznany błąd! Zgłoś się do administracji");
    this.put(ApiFailtureCause.RARE_LIMIT, "&cPrzekroczony limit połączeń z api, odczekaj chwile");
    this.put(ApiFailtureCause.INVALID_NICKNAME, "&cNiepoprawny nick");
    this.put(ApiFailtureCause.NEVER_UPVOTED, "&cNigdy nie głosowałeś!");
    this.put(ApiFailtureCause.SERVER_NOT_FOUND, "&cNie znaleziono serwera na liście!");
    this.put(ApiFailtureCause.INVALID_SERVER_ID, "&cNiepoprawne id serwera! Zgłoś się do administracji");
  }};

  @Comment("Pozostałe wiadomości")
  @CustomKey("user-not-found-message")
  private String userNotFoundMessage = "&8[&4?&8] &cNie znaleziono użytkownika, zgłoś się do administracji";

  @CustomKey("command-only-for-player")
  private String commandOnlyForPlayer = "&8[&4?&8] &cTylko użytkownik może uruchomić tą komendę";

  @CustomKey("receive-reward-message")
  private String receiveRewardMessage = "&8[&a#&8] &aOdebrałeś nagrodę! Następna jutro";

  @CustomKey("user-delay-message")
  private String userDelayMessage = "&8[&4?&8] &cNie możesz jeszcze odebrać nagrody! Pozostało {TIME}";

  @CustomKey("download-information-message")
  private String downloadInformationMessage = "&6Trwa pobieranie danych...";

  @CustomKey("permission-need-message")
  private String permissionNeedMessage = "&8[&4?&8] &cBrak uprawnień! ({PERMISSION})";

  @CustomKey("configuration-reload-message")
  private String configurationReloadMessage = "&8[&a#&8] &aKonfiguracja została przeładowana!";

  @CustomKey("configuration-reload-error-message")
  private String configurationReloadErrorMessage = "&8[&4?&8] &cBłąd w konfiguracji! Sprawdź konsole";

  private String adminInformationMessage = "&8[&a#&8] &aAktualna wersja pluginu: {VERSION} &6(Najnowsza: {UPDATE})";
}
