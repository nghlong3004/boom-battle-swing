package io.nghlong3004.game.component.option;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.SpriteButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;

public class OptionComponent extends GameComponent {

    private SpriteButton homeButton;
    private BufferedImage backgroundOption;
    private final GameComponent audioComponent;

    public OptionComponent(GameContext context, GameComponent audioComponent) {
        super(context);
        loadBackground();
        createSpritesButton();
        this.audioComponent = audioComponent;
    }

    private void loadBackground() {
        backgroundOption = ImageLoader.loadImage(ImageConstant.OPTION_BACKGROUND);
    }

    private void createSpritesButton() {
        int homeX = GAME_WIDTH - URM_BUTTON_SIZE >>> 1;
        int spriteY = GAME_HEIGHT - URM_BUTTON_SIZE * 2;
        homeButton = new SpriteButton(homeX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2, context.getAudio());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            homeButton.setMousePressed(true);
        }
        audioComponent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                context.changeState(GameStateType.MENU);
            }
        }
        homeButton.reset();
        audioComponent.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        homeButton.setMouseOver(false);
        if (homeButton.isMouseOver(e)) {
            homeButton.setMouseOver(true);
        }
        audioComponent.mouseMoved(e);
    }

    @Override
    public void update() {
        homeButton.update();
        audioComponent.update();
    }


    @Override
    public void render(Graphics g) {
        if (backgroundOption != null) {
            g.drawImage(backgroundOption, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        }
        homeButton.render(g);
        audioComponent.render(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioComponent.mouseDragged(e);
    }
}
