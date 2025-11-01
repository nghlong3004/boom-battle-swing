package io.nghlong3004.view.state;

import io.nghlong3004.model.State;
import io.nghlong3004.system.GameStateContextHolder;
import io.nghlong3004.view.GameObject;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public final class StateContext implements GameObject {

    @Getter
    private GameState currentState;

    private final GameState menuState;
    private final PlayingState playingState;
    private final GameState pausedState;

    public StateContext() {
        menuState = new MenuState(this);
        playingState = new PlayingState(this);
        pausedState = new PausedState(this);

        changeState(State.MENU);
    }

    public void windowFocusLost() {
        if (GameStateContextHolder.STATE == State.PLAYING) {
            playingState.getBomber().resetDirection();
        }
    }

    public void changeState(State state) {
        if (currentState != null && state != State.OPTION) {
            currentState.exit();
        }

        switch (state) {
            case MENU -> currentState = menuState;
            case PLAYING -> currentState = playingState;
            case OPTION -> currentState = pausedState;
            case QUIT -> System.exit(0);
        }

        currentState.enter();
        GameStateContextHolder.STATE = state;
    }

    @Override
    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    @Override
    public void render(Graphics g) {
        if (currentState != null) {
            currentState.render(g);
        }
    }


    public void keyPressed(KeyEvent e) {
        if (currentState != null) {
            currentState.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (currentState != null) {
            currentState.keyReleased(e);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (currentState != null) {
            currentState.mouseClicked(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (currentState != null) {
            currentState.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (currentState != null) {
            currentState.mouseReleased(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (currentState != null) {
            currentState.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (currentState != null) {
            currentState.mouseDragged(e);
        }
    }

}
