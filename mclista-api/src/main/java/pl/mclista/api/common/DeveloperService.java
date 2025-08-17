package pl.mclista.api.common;

import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.client.RewardApiClient;
import pl.mclista.api.common.user.UserService;

public interface DeveloperService {

  @NotNull UserService getUserService();

  @NotNull RewardApiClient getRewardApiClient();
}
