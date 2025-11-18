package io.nghlong3004.game.component.menu;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.GameModeButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.MainMenuState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.GameType;
import io.nghlong3004.model.type.MenuType;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH_DEFAULT;
import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

@Slf4j
public class GameTypeComponent extends GameComponent {
    private GameModeButton[] modeButtons;
    private BufferedImage background;

    public GameTypeComponent(GameContext stateContext) {
        super(stateContext);
        loadBackground();
        loadModeButtons();
    }

    private void loadBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadModeButtons() {
        modeButtons = new GameModeButton[2];
        int x = GAME_WIDTH >>> 1;
        int factor = (int) ((MENU_BUTTON_WIDTH_DEFAULT) * SCALE);


        int yOffline = factor * 3 >>> 1;
        modeButtons[0] = new GameModeButton(x, yOffline, GameType.OFFLINE, "OFFLINE", context.getAudio());


        int yOnline = factor * 4 >>> 1;
        modeButtons[1] = new GameModeButton(x, yOnline, GameType.ONLINE, "ONLINE", context.getAudio());
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

        for (GameModeButton button : modeButtons) {
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
                modeButton.startBlinking();
                GameType selectedMode = modeButton.getGameMode();
                context.setGameType(selectedMode);

                if (selectedMode == GameType.ONLINE) {
                    context.changeState(GameStateType.ONLINE);
                    log.info("ONLINE mode selected, going to online lobby");
                }
                else {
                    ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.PLAYER_COUNT);
                    log.info("{} mode selected, proceeding to player count selection", selectedMode.name());
                }
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

    private void reset() {
        for (var modeButton : modeButtons) {
            modeButton.reset();
        }
    }
}
