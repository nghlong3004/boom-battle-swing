package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.input.KeyboardAdapter;

import java.awt.event.KeyEvent;

public abstract class PlayComponent extends GameComponent implements KeyboardAdapter {
    public PlayComponent(GameContext context) {
        super(context);
    }

    public void keyPressed(KeyEvent e) {
    }


    public void keyReleased(KeyEvent e) {
    }
    
}
