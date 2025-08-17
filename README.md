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

## ✅ Aktualizacje

To dopiero jedna z pierwszych wersji pluginu, będzie on rozwijany z dnia na dzień.
<br>
Dzięki mojemu doświadczeniu i pomysłowości plugin niebawem stanie się nie do poznania.

<br>
System aktualizacji informuje administrację o aktualizacjach pluginu w konsoli.
Dostępna jest również komenda /mclista-admin, pozwalająca sprawdzić czy plugin jest aktualny.

## 💛 Moduł API

Api można dodać do projektu maven/gradle
<br>
Aktualne platformy:
- bukkit
- velocity (trwają prace)

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
    <version>1.1.0-beta2</version>
  </dependency>
</dependencies>
```

### Gradle
```gradle
maven {
    name = "mclistaRepositoryReleases"
    url = uri("https://repository.mclista.pl/releases")
}

implementation("pl.mclista:mclista-{platform}-api:1.1.0-beta2")
```
<br>

### Pobieranie DeveloperService do projektu

```java
RegisteredServiceProvider<DeveloperService> provider = Bukkit.getServicesManager().getRegistration(DeveloperService.class);
if (provider != null) {
  DeveloperService developerService = provider.getProvider();
}
```

DeveloperService zwraca UserService oraz RewardApiClient
<br>
Każda akcja zwraca objekt DeveloperAction (jeżeli tego wymaga)
```java
  @NotNull RESULT getResult();

  @Nullable Optional<Throwable> getThrowable();
```
> [!IMPORTANT]
> Throwable może być nullem, za to result nigdy.

### Obsługa eventów

Aktualnie plugin posiada 2 zdarzenia
- PostRewardReceiveEvent (Event wywoływany po odebraniu nagrody)
- PreRewardReceiveEvent (Event wywoływany przed odebraniem, można zablokować)

Event dla każdego modułu posiada przedrostek {platform} czyli np. BukkitPreRewardReceiveEvent
<br>

### UserService

Klasa UserService pozwala operować na użytkownikach, nawet tych offline
<br>
Daje pełną swobodę w zarządzaniem użytkownikami

```java
  void addUser(@NotNull User user);

  void removeUser(@NotNull UUID uuid);

  Optional<User> getUser(@NotNull UUID uuid);

  @NotNull CompletableFuture<DeveloperAction<User>> loadUser(@NotNull UUID uuid);

  // Metoda zwraca użytkownika i go automatycznie zapisuje
  @NotNull CompletableFuture<DeveloperAction<User>> modifyUser(@NotNull UUID uuid, @NotNull Consumer<User> userConsumer);

  @NotNull CompletableFuture<DeveloperAction<Boolean>> saveUser(@NotNull User user);

  @NotNull Set<User> getUsers();

  @NotNull CompletableFuture<DeveloperAction<Set<User>>> loadUsers();
```

RewardApiResponse pozwala wysłać zapytanie z odpowiedzią, czy gracz zagłosował na serwer czy nie, delay trzeba pobrać ręcznie z objektu User

### 💙 Status projektu
![Alt](https://repobeats.axiom.co/api/embed/70650ca5fb9b12b8f5921304cf89af4fc8861c42.svg "Repobeats analytics image")
