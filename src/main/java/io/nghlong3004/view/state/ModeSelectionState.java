package io.nghlong3004.view.state;

import io.nghlong3004.model.GameMode;
import io.nghlong3004.model.State;
import io.nghlong3004.system.GameStateContextHolder;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.button.ModeButton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH_DEFAULT;
import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;


@Slf4j
public class ModeSelectionState implements GameState {

    private ModeButton[] modeButtons;
    private BufferedImage background;
    private final StateContext stateContext;

    protected ModeSelectionState(StateContext stateContext) {
        this.stateContext = stateContext;
        loadBackground();
        loadModeButtons();
    }

    private void loadBackground() {
        background = ImageLoaderUtil.loadImage(BACKGROUND);
    }

    private void loadModeButtons() {
        modeButtons = new ModeButton[2];
        int x = GAME_WIDTH >>> 1;
        int factor = (int) ((MENU_BUTTON_WIDTH_DEFAULT) * SCALE);


        int yOffline = factor * 3 >>> 1;
        modeButtons[0] = new ModeButton(x, yOffline, GameMode.OFFLINE, "OFFLINE");


        int yOnline = factor * 4 >>> 1;
        modeButtons[1] = new ModeButton(x, yOnline, GameMode.ONLINE, "ONLINE");
    }

    @Override
    public void update() {
        for (var modeButton : modeButtons) {
            modeButton.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        g.setColor(Color.WHITE);
        for (ModeButton button : modeButtons) {
            button.render(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var modeButton : modeButtons) {
            if (modeButton.isMouseOver(e)) {
                modeButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (var modeButton : modeButtons) {
            if (modeButton.isMouseOver(e) && modeButton.isMousePressed()) {
                GameMode selectedMode = modeButton.getGameMode();

                if (selectedMode == GameMode.ONLINE) {
                    log.info("Online mode selected, proceeding to skin selection");
                    GameStateContextHolder.GAME_MODE = selectedMode;
                    stateContext.changeState(State.SKIN_SELECTION);
                }
                else {
                    GameStateContextHolder.GAME_MODE = selectedMode;
                    log.info("Selected game mode: {}", selectedMode);
                    stateContext.changeState(State.SKIN_SELECTION);
                }
                break;
            }
        }
        reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var modeButton : modeButtons) {
            modeButton.setMouseOver(false);
        }
        for (var modeButton : modeButtons) {
            if (modeButton.isMouseOver(e)) {
                modeButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateContext.changeState(State.MENU);
        }

        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameStateContextHolder.GAME_MODE = GameMode.OFFLINE;
            stateContext.changeState(State.SKIN_SELECTION);
        }
    }

    @Override
    public void exit() {

    }

    @Override
    public void enter() {

        log.info("Entered mode selection screen");
    }

    private void reset() {
        for (var modeButton : modeButtons) {
            modeButton.reset();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
