package io.nghlong3004.game.component.menu;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.TextMenuButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.MainMenuState;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.MenuType;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_HEIGHT;
import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH;
import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;

public class MenuTypeComponent extends GameComponent {

    private TextMenuButton[] menuButtons;

    public MenuTypeComponent(GameContext context) {
        super(context);
        loadMenuButtons();
    }

    private void loadMenuButtons() {
        menuButtons = new TextMenuButton[3];

        int x = GAME_WIDTH - MENU_BUTTON_WIDTH >>> 1;
        int factor = GAME_HEIGHT - MENU_BUTTON_HEIGHT >>> 1;
        int spacing = MENU_BUTTON_HEIGHT * 5 / 4;

        String[] texts = {"START", "SETTING", "EXIT"};
        GameStateType[] states = {GameStateType.MENU, GameStateType.OPTION, GameStateType.QUIT};

        for (int i = 0; i < 3; i++) {
            int y = factor + i * spacing;
            menuButtons[i] = new TextMenuButton(x, y, texts[i], states[i], context.getAudio());
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
        for (TextMenuButton button : menuButtons) {
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
                if (menuButton.getState() == GameStateType.MENU) {
                    ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.GAME_TYPE);
                    break;
                }
                context.changeState(menuButton.getState());
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

    private void reset() {
        for (var menuButton : menuButtons) {
            menuButton.reset();
        }
    }
}
