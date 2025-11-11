package io.nghlong3004.game.component;

import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.input.MouseAdapter;
import io.nghlong3004.game.input.MouseMotionAdapter;
import io.nghlong3004.game.main.GameLogic;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GameComponent implements GameLogic, MouseAdapter, MouseMotionAdapter {
    protected final GameContext context;
}
