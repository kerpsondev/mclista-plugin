package pl.mclista.core.adapter.transformer;

import java.util.function.Function;

public interface Transformer<SENDER, VALUE> extends Function<SENDER, VALUE> {}
