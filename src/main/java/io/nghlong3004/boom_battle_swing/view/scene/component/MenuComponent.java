package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.GameScene;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import io.nghlong3004.boom_battle_swing.view.scene.button.MenuButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.MENU_BUTTON_WIDTH_DEFAULT;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.BACKGROUND;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.MENU_BACKGROUND;

public class MenuComponent extends AbstractScene implements GameScene {

    private MenuButton[] menuButtons;
    private BufferedImage menuBackground;
    private BufferedImage background;
    private int witdhBackground, heightBackground;
    private int xBackground, yBackground;

    public MenuComponent(GameApplication game) {
        super(game);
        loadMenuBackground();
        loadMenuButtons();
    }

    private void loadMenuBackground() {
        menuBackground = ImageUtil.loadImage(MENU_BACKGROUND);
        background = ImageUtil.loadImage(BACKGROUND);
        witdhBackground = (int) (menuBackground.getWidth() * SCALE);
        heightBackground = (int) (menuBackground.getHeight() * SCALE);
        xBackground = (GAME_WIDTH - witdhBackground) / 2;
        yBackground = (int) (45 * SCALE);
    }

    private void loadMenuButtons() {
        menuButtons = new MenuButton[3];
        menuButtons[0] = new MenuButton(GAME_WIDTH / 2, (int) ((MENU_BUTTON_WIDTH_DEFAULT) * SCALE), 0,
                                        GameState.PLAYING);
        menuButtons[1] = new MenuButton(GAME_WIDTH / 2, (int) (((float) (MENU_BUTTON_WIDTH_DEFAULT * 3) / 2) * SCALE),
                                        1, GameState.OPTION);
        menuButtons[2] = new MenuButton(GAME_WIDTH / 2, (int) ((MENU_BUTTON_WIDTH_DEFAULT * 2) * SCALE), 2,
                                        GameState.QUIT);
    }

    @Override
    public void update() {
        for (var menuButton : menuButtons) {
            menuButton.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        for (MenuButton button : menuButtons) {
            button.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var menuButton : menuButtons) {
            if (isInMenuButton(e, menuButton)) {
                menuButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (var menuButton : menuButtons) {
            if (isInMenuButton(e, menuButton)) {
                menuButton.applyGameState();
                break;
            }
        }
        reset();
    }

    private void reset() {
        for (var menuButton : menuButtons) {
            menuButton.reset();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var menuButton : menuButtons) {
            menuButton.setMouseOver(false);
        }
        for (var menuButton : menuButtons) {
            if (isInMenuButton(e, menuButton)) {
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
