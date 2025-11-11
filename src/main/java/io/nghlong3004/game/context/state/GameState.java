package io.nghlong3004.game.context.state;

import io.nghlong3004.game.input.KeyboardAdapter;
import io.nghlong3004.game.input.MouseAdapter;
import io.nghlong3004.game.input.MouseMotionAdapter;
import io.nghlong3004.game.main.GameLogic;

public interface GameState extends GameLogic, KeyboardAdapter, MouseAdapter, MouseMotionAdapter {

    void on();

    void off();
}
