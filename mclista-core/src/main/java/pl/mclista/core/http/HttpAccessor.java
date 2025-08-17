package pl.mclista.core.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public final class HttpAccessor {

  private static final Gson GSON = new Gson();
  private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
      .callTimeout(20_000, java.util.concurrent.TimeUnit.MILLISECONDS)
      .connectTimeout(20_000, java.util.concurrent.TimeUnit.MILLISECONDS)
      .readTimeout(20_000, java.util.concurrent.TimeUnit.MILLISECONDS)
      .build();

  private HttpAccessor() {}

  public static <V> CompletableFuture<V> makeResponse(
      @NotNull String url,
      @NotNull BiFunction<JsonObject, Integer, V> bodyFunction
  ) {
    CompletableFuture<V> future = new CompletableFuture<>();

    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    CLIENT.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException exception) {
        future.completeExceptionally(exception);
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) {
        try {
          String body = response.body() != null ? response.body().string() : "";
          JsonObject jsonObject = GSON.fromJson(body, JsonObject.class);
          V result = bodyFunction.apply(jsonObject, response.code());
          future.complete(result);
        } catch (IOException exception) {
          future.completeExceptionally(exception);
        }
      }
    });

    return future;
  }
}