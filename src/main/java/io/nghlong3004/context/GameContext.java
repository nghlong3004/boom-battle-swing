package io.nghlong3004.context;

import io.nghlong3004.context.state.*;
import io.nghlong3004.game.GameLogic;
import io.nghlong3004.input.KeyboardAdapter;
import io.nghlong3004.input.MouseAdapter;
import io.nghlong3004.input.MouseMotionAdapter;
import io.nghlong3004.loader.AudioLoader;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.GameType;
import io.nghlong3004.type.MapType;
import io.nghlong3004.type.PlayerCountType;
import io.nghlong3004.type.SkinType;
import lombok.Getter;
import lombok.Setter;
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
    private final AudioLoader audio;
    private final EnumMap<GameStateType, GameState> stateMap;
    @Getter
    private GameStateType state;
    @Getter
    @Setter
    private SkinType[] skinType = new SkinType[2];
    @Getter
    @Setter
    private MapType mapType = MapType.DESERT_MODE;
    @Getter
    @Setter
    private GameType gameType = GameType.OFFLINE;
    @Getter
    @Setter
    private PlayerCountType playerCount = PlayerCountType.ONE_PLAYER;
    @Getter
    @Setter
    private int numberBomber = 1;

    public GameContext() {
        this.stateMap = new EnumMap<>(GameStateType.class);
        this.audio = new AudioLoader();
        loadStates();
        changeState(GameStateType.MENU);
    }

    public void changeState(GameStateType stateType) {
        GameState newState = stateMap.get(stateType);
        if (this.currentState != null && stateType != GameStateType.OPTION) {
            this.currentState.off();
        }
        this.currentState = newState;
        if (state != GameStateType.OPTION) {
            this.currentState.on();
        }
        state = stateType;
    }

    public GameState getGameState(GameStateType stateID) {
        return stateMap.get(stateID);
    }

    private void loadStates() {
        stateMap.put(GameStateType.MENU, new MainMenuState(this));
        stateMap.put(GameStateType.PLAYING, new PlayingState(this));
        stateMap.put(GameStateType.OPTION, new OptionState(this));
        stateMap.put(GameStateType.QUIT, new QuitState(this));
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
