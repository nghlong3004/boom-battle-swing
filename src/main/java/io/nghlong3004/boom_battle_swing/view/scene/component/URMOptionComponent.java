package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.model.GameState;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.MouseScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import io.nghlong3004.boom_battle_swing.view.scene.button.URMButton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;

@Slf4j
public class URMOptionComponent extends AbstractScene implements Scene, MouseScene {

    private URMButton homeButton, replayButton, unpauseButton;

    public URMOptionComponent(GameApplication game) {
        super(game);
        createURMButton();
    }

    private void createURMButton() {
        int homeX = (int) (321 * SCALE);
        int unPauseX = (int) (homeX + URM_BUTTON_SIZE * 6 / 5);
        int relayX = (int) (unPauseX + URM_BUTTON_SIZE * 6 / 5);
        int urmY = (int) (325 * SCALE);
        unpauseButton = new URMButton(unPauseX, urmY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        replayButton = new URMButton(relayX, urmY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
        homeButton = new URMButton(homeX, urmY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isInButton(e, homeButton)) {
            homeButton.setMousePressed(true);
            game.getPlaying().reset();
        }
        else if (isInButton(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        }
        else if (isInButton(e, replayButton)) {
            replayButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isInButton(e, homeButton)) {
            if (homeButton.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        }
        else if (isInButton(e, unpauseButton)) {
            if (unpauseButton.isMousePressed()) {
                GameState.state = GameState.PLAYING;
            }
        }
        else if (isInButton(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                log.info("This is not work");
            }
        }

        homeButton.reset();
        unpauseButton.reset();
        replayButton.reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        homeButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if (isInButton(e, homeButton)) {
            homeButton.setMouseOver(true);
        }
        else if (isInButton(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        }
        else if (isInButton(e, replayButton)) {
            replayButton.setMouseOver(true);
        }
    }

    @Override
    public void update() {
        homeButton.update();
        unpauseButton.update();
        replayButton.update();
    }

    @Override
    public void draw(Graphics g) {
        homeButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
