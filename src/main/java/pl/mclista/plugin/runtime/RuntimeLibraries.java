package pl.mclista.plugin.runtime;

import com.alessiodp.libby.BukkitLibraryManager;
import org.bukkit.plugin.Plugin;

public class RuntimeLibraries {

  private BukkitLibraryManager bukkitLibraryManager;

  public RuntimeLibraries(Plugin plugin) {
    this.bukkitLibraryManager = new BukkitLibraryManager(plugin, "libs");
    this.bukkitLibraryManager.addMavenCentral();
    this.bukkitLibraryManager.loadLibraries(
        new Library(
            "com.zaxxer",
            "HikariCP",
            "7.0.1"
        ).asLibrary(),
        new Library(
            "org.slf4j",
            "slf4j-api",
            "2.0.17"
        ).asLibrary(),
        new Library(
            "org.mariadb.jdbc",
            "mariadb-java-client",
            "3.5.5"
        ).asLibrary(),
        new Library(
            "com.mysql",
            "mysql-connector-j",
            "9.4.0"
        ).asLibrary(),
        new Library(
            "com.google.protobuf",
            "protobuf-java",
            "4.31.1"
        ).asLibrary(),
        new Library(
            "com.google.code.gson",
            "gson",
            "2.11.0"
        ).asLibrary(),
        new Library(
            "com.google.errorprone",
            "error_prone_annotations",
            "2.27.0"
        ).asLibrary(),
        new Library(
            "org.xerial",
            "sqlite-jdbc",
            "3.50.3.0"
        ).asLibrary()
    );
  }

  public class Library {

    private final String groupId;
    private final String artifactId;
    private final String version;

    public Library(String groupId, String artifactId, String version) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
    }

    public com.alessiodp.libby.Library asLibrary() {
      return com.alessiodp.libby.Library.builder()
          .groupId(this.groupId)
          .artifactId(this.artifactId)
          .version(this.version)
          .build();
    }
  }
}
