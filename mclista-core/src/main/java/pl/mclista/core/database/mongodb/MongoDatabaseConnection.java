package pl.mclista.core.database.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import pl.mclista.api.common.user.User;
import pl.mclista.core.configuration.section.sub.MongodbConfiguration;
import pl.mclista.core.database.DatabaseConnection;
import pl.mclista.core.user.UserImpl;

public class MongoDatabaseConnection implements DatabaseConnection {

  private final MongoClient mongoClient;
  private final MongoCollection<Document> usersCollection;

  public MongoDatabaseConnection(@NotNull MongodbConfiguration mongodbConfiguration) {
    this.mongoClient = MongoClients.create(mongodbConfiguration.getConnectionString());

    MongoDatabase database = mongoClient.getDatabase(mongodbConfiguration.getDatabaseName());
    this.usersCollection = database.getCollection("users");

    this.createCollection();
  }

  @Override
  public CompletableFuture<User> loadUser(@NotNull UUID uuid) {
    return CompletableFuture.supplyAsync(() -> {
      Bson filter = Filters.eq("uuid", uuid.toString());
      Document doc = usersCollection.find(filter).first();

      User user = new UserImpl(uuid);
      if (doc != null) {
        long delay = doc.getLong("delay");
        user.setDelay(Instant.ofEpochMilli(delay));
      }

      return user;
    });
  }

  @Override
  public CompletableFuture<Set<User>> loadAllUsers() {
    return CompletableFuture.supplyAsync(() -> {
      Set<User> users = new HashSet<>();
      try (MongoCursor<Document> cursor = usersCollection.find().iterator()) {
        while (cursor.hasNext()) {
          Document doc = cursor.next();
          UUID uuid = UUID.fromString(doc.getString("uuid"));
          long delay = doc.getLong("delay");

          User user = new UserImpl(uuid);
          user.setDelay(Instant.ofEpochMilli(delay));
          users.add(user);
        }
      }
      return users;
    });
  }

  @Override
  public CompletableFuture<Boolean> saveUser(@NotNull User user) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Document doc = new Document("uuid", user.getUniqueId().toString())
            .append("delay", user.getDelay().toEpochMilli());

        usersCollection.replaceOne(
            Filters.eq("uuid", user.getUniqueId().toString()),
            doc,
            new ReplaceOptions().upsert(true)
        );
        return true;
      } catch (Exception exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  @Override
  public CompletableFuture<Boolean> saveAllUsers(@NotNull Collection<User> users) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        List<WriteModel<Document>> operations = new ArrayList<>();

        for (User user : users) {
          Document doc = new Document("uuid", user.getUniqueId().toString())
              .append("delay", user.getDelay().toEpochMilli());

          operations.add(new ReplaceOneModel<>(
              Filters.eq("uuid", user.getUniqueId().toString()),
              doc,
              new ReplaceOptions().upsert(true)
          ));
        }

        if (!operations.isEmpty()) {
          usersCollection.bulkWrite(operations);
        }
        return true;
      } catch (Exception exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  private void createCollection() {
    usersCollection.createIndex(new Document("uuid", 1));
  }

  @Override
  public void shutdown() {
    Optional.ofNullable(this.mongoClient).ifPresent(MongoClient::close);
  }
}
