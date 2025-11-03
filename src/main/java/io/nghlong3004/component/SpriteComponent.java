package io.nghlong3004.component;

import io.nghlong3004.component.button.GameButton;
import io.nghlong3004.component.button.SpriteButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.GameStateID;
import io.nghlong3004.context.state.PlayingState;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.SCALE;

@Slf4j
public class SpriteComponent extends GameComponent {
    private SpriteButton unpauseButton;
    private SpriteButton replayButton;
    private SpriteButton homeButton;

    private final List<GameButton> buttons;

    public SpriteComponent(GameContext context) {
        super(context);
        createSpritesButton();
        buttons = List.of(unpauseButton, replayButton, homeButton);
    }

    private void createSpritesButton() {
        int homeX = (int) (321 * SCALE);
        int unPauseX = (int) (homeX + URM_BUTTON_SIZE * 6 / 5);
        int relayX = (int) (unPauseX + URM_BUTTON_SIZE * 6 / 5);
        int spriteY = (int) (325 * SCALE);
        unpauseButton = new SpriteButton(unPauseX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        replayButton = new SpriteButton(relayX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
        homeButton = new SpriteButton(homeX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
                PlayingState playingState = (PlayingState) context.getState(GameStateID.PLAYING);
                if (playingState != null) {
                    playingState.reset();
                    log.info("Game reset, returning to menu");
                }
                context.changeState(GameStateID.MENU);
            }
        }
        else if (unpauseButton.isMouseOver(e)) {
            if (unpauseButton.isMousePressed()) {
                log.info("Unpausing game, continuing...");
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                PlayingState playingState = (PlayingState) context.getState(GameStateID.PLAYING);
                if (playingState != null) {
                    playingState.reset();
                    log.info("Game reset, restarting...");
                }
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

}
