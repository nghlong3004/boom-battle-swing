package io.nghlong3004.component.menu;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.button.SkinButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.MainMenuState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.MenuType;
import io.nghlong3004.type.SkinType;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

@Slf4j
public class SkinTypeComponent extends GameComponent {
    private SkinButton[] skinButtons;
    private BufferedImage background;

    public SkinTypeComponent(GameContext stateContext) {
        super(stateContext);
        loadBackground();
        loadSkinButtons();
    }

    private void loadBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadSkinButtons() {
        SkinType[] skins = SkinType.values();
        skinButtons = new SkinButton[skins.length];

        int buttonWidth = (int) (120 * SCALE);
        int buttonHeight = (int) (150 * SCALE);
        int spacing = (int) (20 * SCALE);
        int totalWidth = (buttonWidth * skins.length) + (spacing * (skins.length - 1));
        int startX = (GAME_WIDTH - totalWidth) / 2 + buttonWidth / 2;
        int y = GAME_HEIGHT / 2;

        for (int i = 0; i < skins.length; i++) {
            int x = startX + i * (buttonWidth + spacing);
            skinButtons[i] = new SkinButton(x, y, buttonWidth, buttonHeight, skins[i]);
        }
    }

    @Override
    public void update() {
        for (var skinButton : skinButtons) {
            skinButton.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        for (SkinButton button : skinButtons) {
            button.render(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var skinButton : skinButtons) {
            if (skinButton.isMouseOver(e)) {
                skinButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (var skinButton : skinButtons) {
            if (skinButton.isMouseOver(e) && skinButton.isMousePressed()) {
                SkinType selectedSkin = skinButton.getSkin();
                context.setSkinType(selectedSkin);
                log.info("Selected skin: {}", selectedSkin.name());
                ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.MAP_TYPE);
            }
        }
        reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var skinButton : skinButtons) {
            skinButton.setMouseOver(false);
        }
        for (var skinButton : skinButtons) {
            if (skinButton.isMouseOver(e)) {
                skinButton.setMouseOver(true);
                break;
            }
        }
    }

    private void reset() {
        for (var skinButton : skinButtons) {
            skinButton.reset();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
