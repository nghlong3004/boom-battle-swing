package io.nghlong3004.component.play;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.SpriteComponent;
import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.PlayingState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.PlayType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GamePausedComponent extends PlayComponent {
    private BufferedImage backgroundOption;
    private final GameComponent spriteComponent;
    private final GameComponent audioComponent;

    public GamePausedComponent(GameContext stateContext, GameComponent audioComponent) {
        super(stateContext);
        loadBackground();
        this.spriteComponent = new SpriteComponent(stateContext);
        this.audioComponent = audioComponent;
    }

    private void loadBackground() {
        backgroundOption = ImageLoader.loadImage(ImageConstant.OPTION_BACKGROUND);
    }


    @Override
    public void update() {
        spriteComponent.update();
        audioComponent.update();
    }

    @Override
    public void render(Graphics g) {
        if (backgroundOption != null) {
            g.drawImage(backgroundOption, 0, 0, GameConstant.GAME_WIDTH, GameConstant.GAME_HEIGHT, null);
        }
        spriteComponent.render(g);
        audioComponent.render(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        spriteComponent.mousePressed(e);
        audioComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        spriteComponent.mouseReleased(e);
        audioComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        spriteComponent.mouseMoved(e);
        audioComponent.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioComponent.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            ((PlayingState) context.getGameState(GameStateType.PLAYING)).setType(PlayType.PLAYING);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
