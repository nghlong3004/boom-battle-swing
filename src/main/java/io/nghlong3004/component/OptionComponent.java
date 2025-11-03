package io.nghlong3004.component;

import io.nghlong3004.component.button.GameButton;
import io.nghlong3004.component.button.SpriteButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.GameStateID;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.SCALE;

public class OptionComponent extends GameComponent {
    private SpriteButton homeButton;

    private final List<GameButton> buttons;

    public OptionComponent(GameContext context) {
        super(context);
        createSpritesButton();
        buttons = List.of(homeButton);
    }

    private void createSpritesButton() {
        int homeX = (int) (387 * SCALE);
        int spriteY = (int) (325 * SCALE);
        homeButton = new SpriteButton(homeX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                context.changeState(GameStateID.MENU);
            }
        }

        for (var gameButton : buttons) {
            gameButton.reset();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var gameButton : buttons) {
            gameButton.setMouseOver(false);
        }
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void update() {
        for (var gameObject : buttons) {
            gameObject.update();
        }
    }


    @Override
    public void render(Graphics g) {
        for (var gameObject : buttons) {
            gameObject.render(g);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
