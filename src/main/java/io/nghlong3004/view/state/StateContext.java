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
    @Getter
    private final PlayingState playingState;
    private final GameState pausedState;
    private final GameState optionState;
    private final GameState modeSelectionState;
    private final GameState skinSelectionState;
    private final GameState mapSelectionState;

    public StateContext() {
        menuState = new MenuState(this);
        playingState = new PlayingState(this);
        pausedState = new PausedState(this);
        optionState = new OptionState(this);
        modeSelectionState = new ModeSelectionState(this);
        skinSelectionState = new SkinSelectionState(this);
        mapSelectionState = new MapSelectionState(this);

        changeState(State.MENU);
    }

    public void windowFocusLost() {
        if (GameStateContextHolder.STATE == State.PLAYING) {
            playingState.getBomber().resetDirection();
        }
    }

    public void changeState(State state) {
        if (currentState != null && state != State.OPTION && state != State.PAUSED) {
            currentState.exit();
        }

        switch (state) {
            case MENU -> currentState = menuState;
            case MODE_SELECTION -> currentState = modeSelectionState;
            case SKIN_SELECTION -> currentState = skinSelectionState;
            case MAP_SELECTION -> currentState = mapSelectionState;
            case PLAYING -> currentState = playingState;
            case PAUSED -> currentState = pausedState;
            case OPTION -> currentState = optionState;
            case QUIT -> System.exit(0);
        }

        if (!(GameStateContextHolder.STATE == State.PAUSED && state == State.PLAYING)) {
            currentState.enter();
        }
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
