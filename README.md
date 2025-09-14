<p align="center">
  <img src="https://i.imgur.com/ngXnPK7.png" width="500" height="400">
</p>

[![Release](https://img.shields.io/github/v/release/kerpsondev/mclista-plugin.svg)](https://github.com/kerpsondev/mclista-plugin/releases)

Oficjalny plugin napisany dla listy serwerów minecraft mclista.pl
<br>
Prosta konfiguracja, niezawodne działanie, ultra szybki.
<br>

## ⚙️ Wymagania 

- Silnik paper (1.8 - 1.21.8)
- Serwer utworzony na stronie [mclista.pl](https://mclista.pl)
- OPCJONALNIE: Baza danych mysql/mariadb/mongodb

## 💛 Moduł API

Api można dodać do projektu maven/gradle.
<br>
Aktualne wspierane platformy:
- bukkit
- velocity (trwają prace nad modułem)

### Maven

```xml
<repositories>
  <repository>
    <id>mclista-repository-releases</id>
    <name>Repozytorium McLista</name>
    <url>https://repository.mclista.pl/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>pl.mclista</groupId>
    <artifactId>mclista-{platform}-api</artifactId>
    <version>1.1.0</version>
  </dependency>
</dependencies>
```

### Gradle
```gradle
maven {
    name = "mclistaRepositoryReleases"
    url = uri("https://repository.mclista.pl/releases")
}

implementation("pl.mclista:mclista-{platform}-api:1.1.0")
```
<br>

## 🤖 Używanie API

W pierwszej kolejności zalecam pobranie klasy DeveloperService.
<br>
Przykład dla api bukkita:

```java
RegisteredServiceProvider<DeveloperService> provider = Bukkit.getServicesManager().getRegistration(DeveloperService.class);
if (provider != null) {
  DeveloperService developerService = provider.getProvider();
}
```

DeveloperService zwraca dwa objekty, UserService oraz RewardApiClient.
<br>
Każda akcja zwracająca objekt zwraca DeveloperService:
```java
  @NotNull RESULT getResult();

  @Nullable Optional<Throwable> getThrowable();
```
> [!IMPORTANT]
> Result nigdy nie jest nullem

### Obsługa eventów

Aktualnie plugin posiada 2 eventy:
- PostRewardReceiveEvent (Event wywoływany po odebraniu nagrody)
- PreRewardReceiveEvent (Event wywoływany przed odebraniem, można anulować akcję odbioru nagrody i ustawić własne warunki)

Event dla każdego modułu posiada przedrostek {platform} czyli np. BukkitPreRewardReceiveEvent.
<br>

### UserService

Klasa UserService pozwala operować na użytkownikach, nawet tych offline.
<br>
Daje pełną swobodę w zarządzaniem użytkownikami.

```java
  // Dodaje usera do listy
  void addUser(@NotNull User user);

  // Usuwa usera z listy
  void removeUser(@NotNull UUID uuid);

  // Pobiera użytkownika z listy
  Optional<User> getUser(@NotNull UUID uuid);

  // Pobiera użytkownika z bazy danych (np. gdy jest offline)
  @NotNull CompletableFuture<DeveloperAction<User>> loadUser(@NotNull UUID uuid);

  // Zwraca użytkownika i go automatycznie zapisuje po wykonanych operacjach
  @NotNull CompletableFuture<DeveloperAction<User>> modifyUser(@NotNull UUID uuid, @NotNull Consumer<User> userConsumer);

  // Zapisuje użytkownika
  @NotNull CompletableFuture<DeveloperAction<Boolean>> saveUser(@NotNull User user);

  // Pobiera użytkowników online
  @NotNull Set<User> getUsers();

  // Pobiera wszystkich użytkowników
  @NotNull CompletableFuture<DeveloperAction<Set<User>>> loadUsers();
```

### RewardApiClient

Objekt ten pozwala wysłać zapytanie do api i otrzymać odpowiedź, czy gracz zagłosował na liście na nasz serwer czy nie.
<br>
Aby sprawdzić, czy użytkownik odebrał dziś nagrodę trzeba porównać delay z aktualnym czasem z objektu [User](#userservice)
<br>
No, ewentualnie można też robić swój delay, masz wybór!

### 💛 Status projektu
![Alt](https://repobeats.axiom.co/api/embed/70650ca5fb9b12b8f5921304cf89af4fc8861c42.svg "Repobeats analytics image")
