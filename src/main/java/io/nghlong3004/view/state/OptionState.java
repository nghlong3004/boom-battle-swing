package io.nghlong3004.view.state;

import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.component.AudioComponent;
import io.nghlong3004.view.component.MouseComponent;
import io.nghlong3004.view.component.OptionComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.SCALE;

public class OptionState implements GameState {

    private BufferedImage background;
    private int xBackground, yBackground, widthBackground, heightBackground;
    private final MouseComponent optionComponent;
    private final MouseComponent audioComponent;
    private final StateContext stateContext;

    protected OptionState(StateContext stateContext) {
        this.stateContext = stateContext;
        loadBackground();
        this.optionComponent = new OptionComponent(stateContext);
        this.audioComponent = new AudioComponent();
    }

    private void loadBackground() {
        background = ImageLoaderUtil.loadImage(ImageConstant.PAUSE_BACKGROUND);
        widthBackground = (int) (background.getWidth() * SCALE);
        heightBackground = (int) (background.getHeight() * SCALE);
        xBackground = GameConstant.GAME_WIDTH - widthBackground >>> 1;
        yBackground = GameConstant.GAME_HEIGHT - heightBackground >>> 1;
    }


    @Override
    public void update() {
        optionComponent.update();
        audioComponent.update();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, xBackground, yBackground, widthBackground, heightBackground, null);
        optionComponent.render(g);
        audioComponent.render(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        optionComponent.mousePressed(e);
        audioComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        optionComponent.mouseReleased(e);
        audioComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        optionComponent.mouseMoved(e);
        audioComponent.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioComponent.mouseDragged(e);
    }

    @Override
    public void exit() {

    }

    @Override
    public void enter() {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
