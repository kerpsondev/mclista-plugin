package pl.mclista.core.http;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

public final class HttpAccessor {

  private static final Gson GSON = new Gson();
  private static final HttpRequestFactory REQUEST_FACTORY =
      new NetHttpTransport().createRequestFactory();

  private HttpAccessor() {}

  public static <V> CompletableFuture<V> makeResponse(
      @NotNull String url,
      @NotNull BiFunction<JsonObject, Integer, V> bodyFunction
  ) {
    return CompletableFuture.supplyAsync(() -> {
      HttpResponse response = null;
      try {
        HttpRequest request = REQUEST_FACTORY.buildGetRequest(new GenericUrl(url));
        request.setConnectTimeout(20_000);
        request.setReadTimeout(20_000);

        response = request.execute();
        String body = response.parseAsString();
        JsonObject jsonObject = GSON.fromJson(body, JsonObject.class);
        return bodyFunction.apply(jsonObject, response.getStatusCode());

      } catch (IOException exception) {
        throw new RuntimeException(exception);
      } finally {
        if (response != null) {
          try {
            response.disconnect();
          } catch (IOException exception) {
            exception.printStackTrace();
          }
        }
      }
    });
  }
}