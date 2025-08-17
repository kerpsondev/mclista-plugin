package pl.mclista.core.adapter.transformer;

import pl.mclista.api.common.adapter.PlayerAdapter;

public interface PlayerTransformer<PLAYER, SENDER> extends Transformer<SENDER, PlayerAdapter<PLAYER>> {}
