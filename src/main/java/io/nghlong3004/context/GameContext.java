package io.nghlong3004.context;

import io.nghlong3004.audio.AudioPlayer;
import io.nghlong3004.context.state.*;
import io.nghlong3004.game.GameLogic;
import io.nghlong3004.input.KeyboardAdapter;
import io.nghlong3004.input.MouseAdapter;
import io.nghlong3004.input.MouseMotionAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;

import static io.nghlong3004.constant.AudioConstant.CLICK;

@Slf4j
public class GameContext implements GameLogic, KeyboardAdapter, MouseAdapter, MouseMotionAdapter {

    private GameState currentState;
    @Getter
    private final AudioPlayer audio;
    private final EnumMap<GameStateID, GameState> stateMap;

    public GameContext() {
        this.stateMap = new EnumMap<>(GameStateID.class);
        this.audio = new AudioPlayer();
        loadStates();
        changeState(GameStateID.MENU);
    }

    public void changeState(GameStateID stateID) {
        GameState newState = stateMap.get(stateID);
        if (this.currentState != null && stateID != GameStateID.OPTION) {
            this.currentState.off();
        }
        this.currentState = newState;
        if (GameStateContext.STATE != GameStateID.OPTION) {
            this.currentState.on();
        }
        GameStateContext.STATE = stateID;
    }

    public GameState getState(GameStateID stateID) {
        return stateMap.get(stateID);
    }

    private void loadStates() {
        stateMap.put(GameStateID.MENU, new MenuState(this));
        stateMap.put(GameStateID.PLAYING, new PlayingState(this));
        stateMap.put(GameStateID.OPTION, new OptionState(this));
        stateMap.put(GameStateID.SKIN_SELECTION, new SkinSelectionState(this));
        stateMap.put(GameStateID.MAP_SELECTION, new MapSelectionState(this));
        stateMap.put(GameStateID.GAME_TYPE_SELECTION, new GameTypeSelectionState(this));
        stateMap.put(GameStateID.QUIT, new QuitState(this));
    }

    @Override
    public void update() {
        currentState.update();
    }

    @Override
    public void render(Graphics g) {
        currentState.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentState.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentState.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        audio.playEffect(CLICK);
        currentState.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentState.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentState.mouseReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentState.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentState.mouseMoved(e);
    }
}
