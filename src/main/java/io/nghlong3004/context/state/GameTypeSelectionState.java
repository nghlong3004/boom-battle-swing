package io.nghlong3004.context.state;

import io.nghlong3004.component.button.ModeButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.GameStateContext;
import io.nghlong3004.entity.GameType;
import io.nghlong3004.loader.ImageLoader;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH_DEFAULT;
import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

@Slf4j
public class GameTypeSelectionState implements GameState {
    private ModeButton[] modeButtons;
    private BufferedImage background;
    private final GameContext stateContext;

    public GameTypeSelectionState(GameContext stateContext) {
        this.stateContext = stateContext;
        loadBackground();
        loadModeButtons();
    }

    private void loadBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadModeButtons() {
        modeButtons = new ModeButton[2];
        int x = GAME_WIDTH >>> 1;
        int factor = (int) ((MENU_BUTTON_WIDTH_DEFAULT) * SCALE);


        int yOffline = factor * 3 >>> 1;
        modeButtons[0] = new ModeButton(x, yOffline, GameType.OFFLINE, "OFFLINE");


        int yOnline = factor * 4 >>> 1;
        modeButtons[1] = new ModeButton(x, yOnline, GameType.ONLINE, "ONLINE");
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
                GameType selectedMode = modeButton.getGameMode();

                if (selectedMode == GameType.ONLINE) {
                    log.info("Online mode selected, proceeding to skin selection");
                    GameStateContext.GAME_TYPE = selectedMode;
                    stateContext.changeState(GameStateID.SKIN_SELECTION);
                }
                else {
                    GameStateContext.GAME_TYPE = selectedMode;
                    log.info("Selected game mode: {}", selectedMode);
                    stateContext.changeState(GameStateID.SKIN_SELECTION);
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
            stateContext.changeState(GameStateID.MENU);
        }

        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameStateContext.GAME_TYPE = GameType.OFFLINE;
            stateContext.changeState(GameStateID.SKIN_SELECTION);
        }
    }

    @Override
    public void off() {

    }

    @Override
    public void on() {

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
