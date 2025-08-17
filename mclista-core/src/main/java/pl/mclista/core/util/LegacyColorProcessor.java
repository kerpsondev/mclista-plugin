package pl.mclista.core.util;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LegacyColorProcessor implements UnaryOperator<Component> {

  private final static LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
      .character('&')
      .hexCharacter('#')
      .useUnusualXRepeatedCharacterHexFormat()
      .build();

  private Component component(String text) {
    return LEGACY_COMPONENT_SERIALIZER.deserialize(text);
  }

  @Override
  public Component apply(Component component) {
    return component.replaceText(builder -> builder.match(Pattern.compile(".*"))
        .replacement((matchResult, builder1) -> this.component(matchResult.group())));
  }
}
