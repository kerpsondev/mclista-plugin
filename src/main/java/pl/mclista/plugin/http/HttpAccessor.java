package pl.mclista.plugin.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class HttpAccessor {

  private static final Gson GSON = new Gson();
  private static final HttpClient CLIENT = HttpClient.newHttpClient();

  public <V> CompletableFuture<V> makeResponse(String url, BiFunction<JsonObject, Integer, V> bodyFunction) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .timeout(Duration.ofSeconds(20L))
        .build();

    return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
      JsonObject jsonObject = GSON.fromJson(response.body(), JsonObject.class);
      return bodyFunction.apply(jsonObject, response.statusCode());
    });
  }
}