package pl.mclista.core.util;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class Placeholders {

  private final Set<Pair> pairs = new HashSet<>();

  public static Placeholders create() {
    return new Placeholders();
  }

  private Placeholders() {}

  public Placeholders withPair(@NotNull String key, @NotNull String value) {
    this.pairs.add(new Pair(key, value));
    return this;
  }

  public @NotNull String apply(@NotNull String text) {
    for (Pair pair : this.pairs) {
      text = this.replaceIgnoreCase(text, pair.getKey(), pair.getValue());
    }

    return text;
  }

  private String replaceIgnoreCase(
      @NotNull String text,
      @NotNull String search,
      @NotNull String replacement
  ) {
    StringBuilder result = new StringBuilder();
    int searchLen = search.length();
    int index = 0;

    while (index < text.length()) {
      int foundIndex = text.toLowerCase().indexOf(search.toLowerCase(), index);
      if (foundIndex == -1) {
        result.append(text.substring(index));
        break;
      }

      result.append(text, index, foundIndex);
      result.append(replacement);
      index = foundIndex + searchLen;
    }

    return result.toString();
  }

  static class Pair {

    private final String key;
    private final String value;

    Pair(@NotNull String key, @NotNull String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }
}
