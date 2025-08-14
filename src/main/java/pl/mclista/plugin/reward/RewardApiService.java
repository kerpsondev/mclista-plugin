package pl.mclista.plugin.reward;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import pl.mclista.plugin.configuration.PluginConfiguration;
import pl.mclista.plugin.http.HttpAccessor;
import pl.mclista.plugin.reward.RewardApiService.Result.FailureCause;

public class RewardApiService {

  private final static String API_URL = "https://mclista.pl/api/check-upvote/%s?nick=%s";

  private final HttpAccessor httpAccessor;
  private final PluginConfiguration pluginConfiguration;

  public RewardApiService(HttpAccessor httpAccessor, PluginConfiguration pluginConfiguration) {
    this.httpAccessor = httpAccessor;
    this.pluginConfiguration = pluginConfiguration;
  }

  public CompletableFuture<Result> sendApiRequest(Player player) {
    return httpAccessor.makeResponse(
        String.format(API_URL, this.pluginConfiguration.getServerId().toString(), player.getName()),
        (jsonObject, statusCode) -> new Result(
            statusCode == 200,
            FailureCause.parseByStatusCode(statusCode)
        )).exceptionally(exception -> new Result(false, FailureCause.UNKNOWN));
  }

  public static class Result {

    private final boolean success;
    private final FailureCause failureCause;

    public Result(boolean success, FailureCause failureCause) {
      this.success = success;
      this.failureCause = failureCause;
    }

    public boolean isSuccess() {
      return success;
    }

    public Optional<FailureCause> getFailureCause() {
      return Optional.ofNullable(this.failureCause);
    }

    public enum FailureCause {

      UNKNOWN(0),
      RARE_LIMIT(429),
      INVALID_NICKNAME(401),
      NEVER_UPVOTED(404),
      SERVER_NOT_FOUND(402),
      INVALID_SERVER_ID(400);

      private final int statusCode;

      FailureCause(int statusCode) {
        this.statusCode = statusCode;
      }

      public int getStatusCode() {
        return statusCode;
      }

      public static FailureCause parseByStatusCode(int statusCode) {
        if (statusCode == 200) {
          return null;
        }

        for (FailureCause cause : FailureCause.values()) {
          if (cause.getStatusCode() != statusCode) {
            continue;
          }

          return cause;
        }

        return FailureCause.UNKNOWN;
      }
    }
  }
}
