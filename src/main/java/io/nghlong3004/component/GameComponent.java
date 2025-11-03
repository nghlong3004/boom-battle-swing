package io.nghlong3004.component;

import io.nghlong3004.context.GameContext;
import io.nghlong3004.game.GameLogic;
import io.nghlong3004.input.MouseAdapter;
import io.nghlong3004.input.MouseMotionAdapter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GameComponent implements GameLogic, MouseAdapter, MouseMotionAdapter {
    protected final GameContext context;
}
