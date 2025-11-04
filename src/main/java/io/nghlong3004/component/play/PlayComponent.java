package io.nghlong3004.component.play;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.input.KeyboardAdapter;

public abstract class PlayComponent extends GameComponent implements KeyboardAdapter {
    public PlayComponent(GameContext context) {
        super(context);
    }
}
