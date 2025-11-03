package io.nghlong3004.context.state;

import io.nghlong3004.component.button.MenuButton;
import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.GameStateContext;
import io.nghlong3004.loader.ImageLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH_DEFAULT;
import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

public class MenuState implements GameState {

    private MenuButton[] menuButtons;
    private BufferedImage background;

    private final GameContext stateContext;

    public MenuState(GameContext stateContext) {
        this.stateContext = stateContext;
        loadMenuBackground();
        loadMenuButtons();
    }

    private void loadMenuBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadMenuButtons() {
        menuButtons = new MenuButton[3];
        int x = GAME_WIDTH >>> 1;
        int factor = (int) ((MENU_BUTTON_WIDTH_DEFAULT) * SCALE);

        GameStateID[] states = {GameStateID.GAME_TYPE_SELECTION, GameStateID.OPTION, GameStateID.QUIT};
        for (int i = 0; i < 3; ++i) {
            int y = factor * (i + 2) >>> 1;
            menuButtons[i] = new MenuButton(x, y, i, states[i]);
        }
    }

    @Override
    public void update() {
        for (var menuButton : menuButtons) {
            menuButton.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        for (MenuButton button : menuButtons) {
            button.render(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var menuButton : menuButtons) {
            if (menuButton.isMouseOver(e)) {
                menuButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (var menuButton : menuButtons) {
            if (menuButton.isMouseOver(e)) {
                stateContext.changeState(menuButton.getState());
                break;
            }
        }
        reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var menuButton : menuButtons) {
            menuButton.setMouseOver(false);
        }
        for (var menuButton : menuButtons) {
            if (menuButton.isMouseOver(e)) {
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            stateContext.changeState(GameStateID.GAME_TYPE_SELECTION);
        }
    }

    @Override
    public void off() {
        if (GameStateContext.STATE != GameStateID.OPTION) {
            stateContext.getAudio().stopSong();
        }
    }

    @Override
    public void on() {
        stateContext.getAudio().playSong(AudioConstant.MENU);
    }

    private void reset() {
        for (var menuButton : menuButtons) {
            menuButton.reset();
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
