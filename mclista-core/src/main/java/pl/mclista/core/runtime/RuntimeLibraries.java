package pl.mclista.core.runtime;

import com.alessiodp.libby.Library;
import com.alessiodp.libby.LibraryManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RuntimeLibraries {

  private final ClassLoader classLoader;
  private final LibraryManager libraryManager;

  public RuntimeLibraries(@NotNull ClassLoader classLoader, @NotNull LibraryManager libraryManager) {
    this.classLoader = classLoader;
    this.libraryManager = libraryManager;
    this.libraryManager.addMavenCentral();
  }

  public void loadLibraries() {
    Gson gson = new Gson();

    try (InputStream inputStream = classLoader.getResourceAsStream("dependencies.json")) {
      Reader reader = new InputStreamReader(inputStream);
      List<Library> libraries = new ArrayList<>();

      JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
      jsonArray.forEach(jsonElement -> {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        libraries.add(Library.builder()
                .groupId(jsonObject.get("group-id").getAsString())
                .artifactId(jsonObject.get("artifact-id").getAsString())
                .version(jsonObject.get("version").getAsString())
            .build());
      });

      this.libraryManager.loadLibraries(libraries.toArray(new Library[0]));
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
