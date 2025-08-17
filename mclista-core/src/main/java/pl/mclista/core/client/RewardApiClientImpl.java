package pl.mclista.core.client;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.client.ApiFailtureCause;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.client.RewardApiResponse;
import pl.mclista.api.common.developer.DeveloperAction;
import pl.mclista.core.configuration.section.PluginConfiguration;
import pl.mclista.core.developer.DeveloperActionImpl;
import pl.mclista.core.http.HttpAccessor;

public class RewardApiClientImpl implements RewardApiClient {

  private final static String API_URL = "https://mclista.pl/api/check-upvote/%s?nick=%s";
  private final PluginConfiguration pluginConfiguration;

  public RewardApiClientImpl(@NotNull PluginConfiguration pluginConfiguration) {
    this.pluginConfiguration = pluginConfiguration;
  }

  @Override
  public CompletableFuture<DeveloperAction<RewardApiResponse>> sendRequest(@NotNull String nickname) {
    String url = String.format(API_URL, pluginConfiguration.getServerId().toString(), nickname);

    return HttpAccessor.makeResponse(
        url,
        (jsonObject, statusCode) -> (DeveloperAction<RewardApiResponse>) new DeveloperActionImpl<>(
            (RewardApiResponse) new RewardApiResponseImpl(statusCode),
            null
        )
    ).exceptionally(throwable -> new DeveloperActionImpl<>(
        new RewardApiResponseImpl(false, ApiFailtureCause.UNKNOWN),
        throwable
    ));
  }
}
