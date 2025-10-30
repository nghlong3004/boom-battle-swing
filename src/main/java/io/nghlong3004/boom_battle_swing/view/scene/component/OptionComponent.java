package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.constant.GameConstant;
import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.MouseScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;

@Slf4j
public class OptionComponent extends AbstractScene implements Scene, MouseScene {

    private BufferedImage background;
    private int xBackground, yBackground, widthBackground, heightBackground;
    private final URMOptionalComponent urmOptionalComponent;
    private final AudioOptionComponent audioOptionComponent;

    public OptionComponent(GameApplication game) {
        super(game);
        loadBackground();
        this.audioOptionComponent = new AudioOptionComponent(game);
        this.urmOptionalComponent = new URMOptionalComponent(game);
    }

    private void loadBackground() {
        background = ImageUtil.loadImage(ImageConstant.PAUSE_BACKGROUND);
        widthBackground = (int) (background.getWidth() * SCALE);
        heightBackground = (int) (background.getHeight() * SCALE);
        xBackground = GameConstant.GAME_WIDTH - widthBackground >>> 1;
        yBackground = GameConstant.GAME_HEIGHT - heightBackground >>> 1;
    }


    @Override
    public void update() {
        urmOptionalComponent.update();
        audioOptionComponent.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background, xBackground, yBackground, widthBackground, heightBackground, null);
        urmOptionalComponent.draw(g);
        audioOptionComponent.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        urmOptionalComponent.mousePressed(e);
        audioOptionComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        urmOptionalComponent.mouseReleased(e);
        audioOptionComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        urmOptionalComponent.mouseMoved(e);
        audioOptionComponent.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioOptionComponent.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
