package io.nghlong3004.game.context.state;

import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.offline.OfflineOverComponent;
import io.nghlong3004.game.component.offline.OfflinePausedComponent;
import io.nghlong3004.game.component.offline.OfflinePlayComponent;
import io.nghlong3004.game.component.offline.OfflineWinComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.model.type.OfflineType;
import lombok.Getter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

public class OfflineState implements GameState {

    private final GameContext gameContext;
    private final Map<OfflineType, GameComponent> gameComponentMap;
    @Getter
    private OfflineType type;
    private OfflineType previousType;

    public OfflineState(GameContext gameContext, GameComponent audioComponent) {
        this.gameContext = gameContext;
        this.gameComponentMap = new EnumMap<>(OfflineType.class);
        loadGameComponentMap(audioComponent);
    }

    private void loadGameComponentMap(GameComponent audioComponent) {
        this.gameComponentMap.put(OfflineType.WIN, new OfflineWinComponent(gameContext));
        this.gameComponentMap.put(OfflineType.OVER, new OfflineOverComponent(gameContext));
        this.gameComponentMap.put(OfflineType.PAUSED, new OfflinePausedComponent(gameContext, audioComponent));
        this.gameComponentMap.put(OfflineType.PLAYING, new OfflinePlayComponent(gameContext));
        this.type = OfflineType.PLAYING;
        this.previousType = OfflineType.PLAYING;
    }

    public void setType(OfflineType newType) {
        if (this.type != newType) {
            this.previousType = this.type;
            this.type = newType;

            if (newType == OfflineType.OVER) {
                gameContext.getAudio()
                           .playSong(AudioConstant.LOSE);
                if (gameComponentMap.get(OfflineType.OVER) instanceof OfflineOverComponent) {
                    ((OfflineOverComponent) gameComponentMap.get(OfflineType.OVER)).resetAnimation();
                }
            }
            else if (newType == OfflineType.WIN) {
                gameContext.getAudio()
                           .playSong(AudioConstant.VICTORY);
                if (gameComponentMap.get(OfflineType.WIN) instanceof OfflineWinComponent) {
                    ((OfflineWinComponent) gameComponentMap.get(OfflineType.WIN)).resetAnimation();
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
        ((OfflinePlayComponent) gameComponentMap.get(type)).play();
    }

    @Override
    public void off() {
        this.type = OfflineType.PLAYING;
        this.previousType = OfflineType.PLAYING;
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
        if (type == OfflineType.OVER || type == OfflineType.WIN) {
            gameComponentMap.get(OfflineType.PLAYING)
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
            type = OfflineType.PAUSED;
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
        ((OfflinePlayComponent) gameComponentMap.get(OfflineType.PLAYING)).exit();
        ((OfflinePlayComponent) gameComponentMap.get(OfflineType.PLAYING)).play();
        this.type = OfflineType.PLAYING;
        this.previousType = OfflineType.PLAYING;
    }

    public GameComponent getComponent(OfflineType playType) {
        return gameComponentMap.get(playType);
    }

    public void unpause() {
        ((OfflinePlayComponent) gameComponentMap.get(OfflineType.PLAYING)).getGameManager()
                                                                          .getAgentManager()
                                                                          .pause();
    }
}
