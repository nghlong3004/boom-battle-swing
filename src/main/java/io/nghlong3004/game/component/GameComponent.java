package io.nghlong3004.game.component;

import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.input.MouseAdapter;
import io.nghlong3004.game.input.MouseMotionAdapter;
import io.nghlong3004.game.main.GameLogic;
import lombok.RequiredArgsConstructor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@RequiredArgsConstructor
public abstract class GameComponent implements GameLogic, MouseAdapter, MouseMotionAdapter {
    protected final GameContext context;

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }


    public void keyReleased(KeyEvent e) {
    }
}
