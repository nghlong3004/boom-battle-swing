package io.nghlong3004.context.state;

import io.nghlong3004.component.GameOverComponent;
import io.nghlong3004.component.GamePausedComponent;
import io.nghlong3004.component.GameWinComponent;
import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.GameStateContext;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayingState implements GameState {

    private final GameContext gameContext;
    private final GameWinComponent gameWin;
    private final GameOverComponent gameOver;
    private final GamePausedComponent gamePaused;
    @Setter
    private boolean isOver, isWin, isPaused;

    public PlayingState(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameWin = new GameWinComponent(gameContext);
        this.gameOver = new GameOverComponent(gameContext);
        this.gamePaused = new GamePausedComponent(gameContext);
    }

    public void reset() {
        isOver = false;
        isWin = false;
        isPaused = false;
    }

    @Override
    public void on() {
        gameContext.getAudio().playSong(GameStateContext.MAP_TYPE.id);
        gameContext.getAudio().playEffect(AudioConstant.START);
    }

    @Override
    public void off() {
        reset();
        gameContext.getAudio().stopSong();
    }

    @Override
    public void update() {
        if (isWin) {
            gameWin.update();
        }
        else if (isOver) {
            gameOver.update();
        }
        else if (isPaused) {
            gamePaused.update();
        }
    }

    @Override
    public void render(Graphics g) {
        if (isWin) {
            gameWin.render(g);
        }
        else if (isOver) {
            gameOver.render(g);
        }
        else if (isPaused) {
            gamePaused.render(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isPaused) {
            gamePaused.keyPressed(e);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            isPaused = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isWin) {
            gameWin.mousePressed(e);
        }
        else if (isOver) {
            gameOver.mousePressed(e);
        }
        else if (isPaused) {
            gamePaused.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isWin) {
            gameWin.mouseReleased(e);
        }
        else if (isOver) {
            gameOver.mouseReleased(e);
        }
        else if (isPaused) {
            gamePaused.mouseReleased(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isWin) {
            gameWin.mouseDragged(e);
        }
        else if (isOver) {
            gameOver.mouseDragged(e);
        }
        else if (isPaused) {
            gamePaused.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isWin) {
            gameWin.mouseMoved(e);
        }
        else if (isOver) {
            gameOver.mouseMoved(e);
        }
        else if (isPaused) {
            gamePaused.mouseMoved(e);
        }
    }
}
