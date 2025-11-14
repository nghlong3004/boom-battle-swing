package io.nghlong3004.game.component.menu;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.PlayerCountButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.MainMenuState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.MenuType;
import io.nghlong3004.model.type.PlayerCountType;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ButtonConstant.MENU_BUTTON_WIDTH_DEFAULT;
import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.*;

@Slf4j
public class PlayerCountComponent extends GameComponent {
    private PlayerCountButton[] playerCountButtons;
    private BufferedImage background;

    public PlayerCountComponent(GameContext stateContext) {
        super(stateContext);
        loadBackground();
        loadPlayerCountButtons();
    }

    private void loadBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadPlayerCountButtons() {
        playerCountButtons = new PlayerCountButton[2];
        int x = GAME_WIDTH >>> 1;
        int factor = (int) ((MENU_BUTTON_WIDTH_DEFAULT) * SCALE);

        int y1P = factor * 3 >>> 1;
        playerCountButtons[0] = new PlayerCountButton(x, y1P, PlayerCountType.ONE_PLAYER, BUTTON_1P, BUTTON_1P_TOUCH);

        int y2P = factor * 4 >>> 1;
        playerCountButtons[1] = new PlayerCountButton(x, y2P, PlayerCountType.TWO_PLAYER, BUTTON_2P, BUTTON_2P_TOUCH);
    }

    @Override
    public void update() {
        for (var button : playerCountButtons) {
            button.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        for (PlayerCountButton button : playerCountButtons) {
            button.render(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var button : playerCountButtons) {
            if (button.isMouseOver(e)) {
                button.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (var button : playerCountButtons) {
            if (button.isMouseOver(e) && button.isMousePressed()) {
                button.startBlinking();

                PlayerCountType selectedCount = button.getPlayerCount();
                context.setPlayerCount(selectedCount);

                // Set number of bombers based on player count
                if (selectedCount == PlayerCountType.ONE_PLAYER) {
                    context.setNumberBomber(1);
                    log.info("1P mode selected, proceeding to skin selection");
                }
                else {
                    context.setNumberBomber(2);
                    log.info("2P mode selected, proceeding to skin selection for P1 and P2");
                }

                ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.SKIN_TYPE);
            }
        }
        reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var button : playerCountButtons) {
            button.setMouseOver(false);
        }
        for (var button : playerCountButtons) {
            if (button.isMouseOver(e)) {
                button.setMouseOver(true);
                break;
            }
        }
    }

    private void reset() {
        for (var button : playerCountButtons) {
            button.reset();
        }
    }
}
