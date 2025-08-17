package pl.mclista.core.util;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class MapUtil {

  public static <K, V> Map<K, V> createMap(Pair<K, V>... pairs) {
    return new HashMap<K, V>() {{
      for (Pair<K, V> pair : pairs) {
        this.put(pair.getK(), pair.getV());
      }
    }};
  }

  public static class Pair<K, V> {

    private final K k;
    private final V v;

    public Pair(@NotNull K k, @NotNull V v) {
      this.k = k;
      this.v = v;
    }

    public K getK() {
      return k;
    }

    public V getV() {
      return v;
    }
  }
}
