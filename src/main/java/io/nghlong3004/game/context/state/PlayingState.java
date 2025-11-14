package io.nghlong3004.game.context.state;

import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.offline.*;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.model.type.PlayType;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

public class PlayingState implements GameState {

    private final GameContext gameContext;
    private final Map<PlayType, PlayComponent> gameComponentMap;
    @Getter
    private PlayType type;
    private PlayType previousType;

    public PlayingState(GameContext gameContext, GameComponent audioComponent) {
        this.gameContext = gameContext;
        this.gameComponentMap = new EnumMap<>(PlayType.class);
        loadGameComponentMap(audioComponent);
    }

    private void loadGameComponentMap(GameComponent audioComponent) {
        this.gameComponentMap.put(PlayType.WIN, new GameWinComponent(gameContext));
        this.gameComponentMap.put(PlayType.OVER, new GameOverComponent(gameContext));
        this.gameComponentMap.put(PlayType.PAUSED, new GamePausedComponent(gameContext, audioComponent));
        this.gameComponentMap.put(PlayType.PLAYING, new GamePlayComponent(gameContext));
        this.type = PlayType.PLAYING;
        this.previousType = PlayType.PLAYING;
    }

    public void setType(PlayType newType) {
        if (this.type != newType) {
            this.previousType = this.type;
            this.type = newType;

            if (newType == PlayType.OVER) {
                gameContext.getAudio()
                           .playSong(AudioConstant.LOSE);
                if (gameComponentMap.get(PlayType.OVER) instanceof GameOverComponent) {
                    ((GameOverComponent) gameComponentMap.get(PlayType.OVER)).resetAnimation();
                }
            }
            else if (newType == PlayType.WIN) {
                gameContext.getAudio()
                           .playSong(AudioConstant.VICTORY);
                if (gameComponentMap.get(PlayType.WIN) instanceof GameWinComponent) {
                    ((GameWinComponent) gameComponentMap.get(PlayType.WIN)).resetAnimation();
                }
            }
        }
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
        this.previousType = PlayType.PLAYING;
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
        if (type == PlayType.OVER || type == PlayType.WIN) {
            gameComponentMap.get(PlayType.PLAYING)
                            .render(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        }

        gameComponentMap.get(type)
                        .render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameComponentMap.get(type)
                        .keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            type = PlayType.PAUSED;
            unpause();
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
        ((GamePlayComponent) gameComponentMap.get(PlayType.PLAYING)).play();
        this.type = PlayType.PLAYING;
        this.previousType = PlayType.PLAYING;
    }

    public PlayComponent getComponent(PlayType playType) {
        return gameComponentMap.get(playType);
    }

    public void unpause() {
        ((GamePlayComponent) gameComponentMap.get(PlayType.PLAYING)).getGameManager()
                                                                    .getAgentManager()
                                                                    .pause();
    }
}
