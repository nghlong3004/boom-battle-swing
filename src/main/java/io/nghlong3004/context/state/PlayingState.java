package io.nghlong3004.context.state;

import io.nghlong3004.component.play.*;
import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.type.PlayType;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

public class PlayingState implements GameState {

    private final GameContext gameContext;
    private final Map<PlayType, PlayComponent> gameComponentMap;
    @Setter
    private PlayType type;

    public PlayingState(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameComponentMap = new EnumMap<>(PlayType.class);
        loadGameComponentMap();
    }

    private void loadGameComponentMap() {
        this.gameComponentMap.put(PlayType.WIN, new GameWinComponent(gameContext));
        this.gameComponentMap.put(PlayType.OVER, new GameOverComponent(gameContext));
        this.gameComponentMap.put(PlayType.PAUSED, new GamePausedComponent(gameContext));
        this.gameComponentMap.put(PlayType.PLAYING, new GamePlayComponent(gameContext));
        this.type = PlayType.PLAYING;
    }

    @Override
    public void on() {
        gameContext.getAudio()
                   .playSong(gameContext.getMapType().id);
        gameContext.getAudio()
                   .playEffect(AudioConstant.START);
        ((GamePlayComponent) gameComponentMap.get(type)).play();
    }

    @Override
    public void off() {
        this.type = PlayType.PLAYING;
        gameContext.getAudio()
                   .stopSong();
    }

    @Override
    public void update() {
        gameComponentMap.get(type)
                        .update();
    }

    @Override
    public void render(Graphics g) {
        gameComponentMap.get(type)
                        .render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameComponentMap.get(type)
                        .keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            type = PlayType.PAUSED;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameComponentMap.get(type)
                        .keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameComponentMap.get(type)
                        .mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseMoved(e);
    }

    public void replay() {

    }
}
