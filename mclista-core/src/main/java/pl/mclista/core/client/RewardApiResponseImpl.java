package pl.mclista.core.client;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.client.ApiFailtureCause;
import pl.mclista.api.common.client.RewardApiResponse;

public class RewardApiResponseImpl implements RewardApiResponse {

  private final boolean success;
  private final ApiFailtureCause apiFailureCause;

  public RewardApiResponseImpl(int statusCode) {
    this.success = statusCode == 200;
    this.apiFailureCause = ApiFailtureCause.parseByStatusCode(statusCode);
  }

  public RewardApiResponseImpl(boolean success, @NotNull ApiFailtureCause failureCause) {
    this.success = success;
    this.apiFailureCause = failureCause;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }

  @Override
  public Optional<ApiFailtureCause> getCause() {
    return Optional.ofNullable(this.apiFailureCause);
  }
}
