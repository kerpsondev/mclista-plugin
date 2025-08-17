package pl.mclista.api.common.client;

import org.jetbrains.annotations.Nullable;

public enum ApiFailtureCause {

  UNKNOWN(0),
  RARE_LIMIT(429),
  INVALID_NICKNAME(401),
  NEVER_UPVOTED(404),
  SERVER_NOT_FOUND(402),
  INVALID_SERVER_ID(400);

  private final int statusCode;

  ApiFailtureCause(int statusCode) {
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public static @Nullable ApiFailtureCause parseByStatusCode(int statusCode) {
    if (statusCode == 200) {
      return null;
    }

    for (ApiFailtureCause apiFailtureCause : ApiFailtureCause.values()) {
      if (apiFailtureCause.getStatusCode() != statusCode) {
        continue;
      }

      return apiFailtureCause;
    }

    return ApiFailtureCause.UNKNOWN;
  }
}