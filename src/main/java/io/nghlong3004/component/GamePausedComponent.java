package io.nghlong3004.component;

import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.GameStateID;
import io.nghlong3004.context.state.PlayingState;
import io.nghlong3004.input.KeyboardAdapter;
import io.nghlong3004.loader.ImageLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.SCALE;

public class GamePausedComponent extends GameComponent implements KeyboardAdapter {
    private BufferedImage background;
    private int xBackground, yBackground, widthBackground, heightBackground;
    private final GameComponent spriteComponent;
    private final GameComponent audioComponent;

    public GamePausedComponent(GameContext stateContext) {
        super(stateContext);
        loadBackground();
        this.spriteComponent = new SpriteComponent(stateContext);
        this.audioComponent = new AudioComponent(stateContext);
    }

    private void loadBackground() {
        background = ImageLoader.loadImage(ImageConstant.PAUSE_BACKGROUND);
        widthBackground = (int) (background.getWidth() * SCALE);
        heightBackground = (int) (background.getHeight() * SCALE);
        xBackground = GameConstant.GAME_WIDTH - widthBackground >>> 1;
        yBackground = GameConstant.GAME_HEIGHT - heightBackground >>> 1;
    }


    @Override
    public void update() {
        spriteComponent.update();
        audioComponent.update();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, xBackground, yBackground, widthBackground, heightBackground, null);
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
            ((PlayingState) context.getState(GameStateID.PLAYING)).setPaused(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
