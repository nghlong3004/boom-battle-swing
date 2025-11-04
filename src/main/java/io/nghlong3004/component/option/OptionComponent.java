package io.nghlong3004.component.option;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.button.SpriteButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.type.GameStateType;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;

public class OptionComponent extends GameComponent {

    private SpriteButton homeButton;

    public OptionComponent(GameContext context) {
        super(context);
        createSpritesButton();
    }

    private void createSpritesButton() {
        int homeX = GAME_WIDTH - URM_BUTTON_SIZE >>> 1;
        int spriteY = GAME_HEIGHT - URM_BUTTON_SIZE * 2;
        homeButton = new SpriteButton(homeX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            homeButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                context.changeState(GameStateType.MENU);
            }
        }
        homeButton.reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        homeButton.setMouseOver(false);
        if (homeButton.isMouseOver(e)) {
            homeButton.setMouseOver(true);
        }
    }

    @Override
    public void update() {
        homeButton.update();
    }


    @Override
    public void render(Graphics g) {
        homeButton.render(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
