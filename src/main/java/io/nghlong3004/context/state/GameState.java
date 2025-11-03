package io.nghlong3004.context.state;

import io.nghlong3004.game.GameLogic;
import io.nghlong3004.input.KeyboardAdapter;
import io.nghlong3004.input.MouseAdapter;
import io.nghlong3004.input.MouseMotionAdapter;

public interface GameState extends GameLogic, KeyboardAdapter, MouseAdapter, MouseMotionAdapter {

    void on();

    void off();
}
