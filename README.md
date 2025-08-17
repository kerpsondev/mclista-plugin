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
- OPCJONALNIE: Baza danych mysql/mariadb

## ✅ Aktualizacje

To dopiero jedna z pierwszych wersji pluginu, będzie on rozwijany z dnia na dzień.
<br>
Dzięki mojemu doświadczeniu i pomysłowości plugin niebawem stanie się nie do poznania.

<br>
System aktualizacji informuje administrację o aktualizacjach pluginu w konsoli.
Dostępna jest również komenda /mclista-admin, pozwalająca sprawdzić czy plugin jest aktualny.

## 💙 Obsługa api

Api można dodać do projektu maven/gradle
Aktualne platformy:
- bukkit

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
    <version>1.1.0-beta1</version>
  </dependency>
</dependencies>
```

### Gradle
```gradle
maven {
    name = "mclistaRepositoryReleases"
    url = uri("https://repository.mclista.pl/releases")
}

implementation("pl.mclista:mclista-{platform}-api:1.1.0-beta1")
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

