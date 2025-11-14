package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.input.KeyboardAdapter;

public abstract class PlayComponent extends GameComponent implements KeyboardAdapter {
    public PlayComponent(GameContext context) {
        super(context);
    }
}
