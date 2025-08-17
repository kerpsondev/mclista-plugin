package pl.mclista.api.common.client;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.developer.DeveloperAction;

public interface RewardApiClient {

  CompletableFuture<DeveloperAction<RewardApiResponse>> sendRequest(@NotNull String nickname);
}
