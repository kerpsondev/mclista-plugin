package pl.mclista.core.updater;

import com.google.gson.JsonElement;
import java.util.concurrent.CompletableFuture;
import pl.mclista.core.http.HttpAccessor;

public class PluginUpdater {

  private final static String REPOSITORY = "mclista-plugin";

  private final static String CURRENT_VERSION = "1.1.1";

  private String newestVersion;

  public PluginUpdater() {
    this.newestVersion = "Unknown";

    this.checkNewestVersion();
  }

  public String getCurrentVersion() {
    return CURRENT_VERSION;
  }

  public String getNewestVersion() {
    return newestVersion;
  }

  public boolean needUpdate() {
    return !newestVersion.equalsIgnoreCase("Unknown") && !CURRENT_VERSION.equalsIgnoreCase(this.newestVersion);
  }

  public CompletableFuture<String> checkNewestVersion() {
    return HttpAccessor.makeResponse(
        String.format("https://api.github.com/repos/kerpsondev/%s/releases/latest", REPOSITORY),
        (jsonObject, statusCode) -> {
          JsonElement jsonElement = jsonObject.get("tag_name");

          this.newestVersion = jsonElement.getAsString();
          return newestVersion;
    });
  }
}
