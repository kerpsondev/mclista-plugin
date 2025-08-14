package pl.mclista.plugin.updater;

import com.google.gson.JsonElement;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.PluginDescriptionFile;
import pl.mclista.plugin.http.HttpAccessor;

public class PluginUpdater {

  private final static String REPOSITORY = "mclista-plugin";

  private final String currentVersion;
  private final HttpAccessor httpAccessor;

  private String newestVersion;

  public PluginUpdater(HttpAccessor httpAccessor, PluginDescriptionFile pluginDescriptionFile) {
    this.httpAccessor = httpAccessor;
    this.currentVersion = pluginDescriptionFile.getVersion();
    this.newestVersion = "Unknown";

    this.checkNewestVersion();
  }

  public String getCurrentVersion() {
    return currentVersion;
  }

  public String getNewestVersion() {
    return newestVersion;
  }

  public boolean needUpdate() {
    return !newestVersion.equalsIgnoreCase("Unknown") && !currentVersion.equalsIgnoreCase(this.newestVersion);
  }

  public CompletableFuture<String> checkNewestVersion() {
    return this.httpAccessor.makeResponse(String.format("https://api.github.com/repos/kerpsondev/%s/releases/latest", REPOSITORY), (jsonObject, statusCode) -> {
      JsonElement jsonElement = jsonObject.get("tag_name");

      this.newestVersion = jsonElement.getAsString();
      return newestVersion;
    });
  }
}
