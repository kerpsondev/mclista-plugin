package pl.mclista.api.common.client;

import java.util.Optional;

public interface RewardApiResponse {

  boolean isSuccess();

  Optional<ApiFailtureCause> getCause();
}
