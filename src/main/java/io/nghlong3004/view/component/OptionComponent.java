package io.nghlong3004.view.component;


import io.nghlong3004.model.State;
import io.nghlong3004.view.GameObject;
import io.nghlong3004.view.button.GameButton;
import io.nghlong3004.view.button.SpriteButton;
import io.nghlong3004.view.state.StateContext;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.SCALE;

@Slf4j
public class OptionComponent implements MouseComponent {

    private SpriteButton homeButton;

    private final List<GameButton> buttons;
    private final List<GameObject> objects;

    private final StateContext context;

    public OptionComponent(StateContext context) {
        this.context = context;
        createSpritesButton();
        buttons = List.of(homeButton);
        objects = List.of(homeButton);
    }

    private void createSpritesButton() {
        // Center the home button since there's only one button
        int homeX = (int) (387 * SCALE); // Centered position
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
                context.changeState(State.MENU);
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
        for (var gameObject : objects) {
            gameObject.update();
        }
    }

    @Override
    public void render(Graphics g) {
        for (var gameObject : objects) {
            gameObject.render(g);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

}
