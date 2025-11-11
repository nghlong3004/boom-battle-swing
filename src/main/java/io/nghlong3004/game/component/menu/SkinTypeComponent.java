package io.nghlong3004.game.component.menu;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.SkinButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.MainMenuState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.MenuType;
import io.nghlong3004.model.type.SkinType;
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
    private int currentPlayerSelecting = 0;
    private SkinType[] selectedSkins = new SkinType[2];

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

        if (context.getNumberBomber() == 2) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String title = currentPlayerSelecting == 0 ? "PLAYER 1 - SELECT SKIN" : "PLAYER 2 - SELECT SKIN";
            FontMetrics fm = g.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            int titleX = (GAME_WIDTH - titleWidth) / 2;
            int titleY = (int) (100 * SCALE);

            g.setColor(Color.BLACK);
            g.drawString(title, titleX + 3, titleY + 3);

            g.setColor(new Color(255, 200, 0));
            g.drawString(title, titleX, titleY);
        }

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
                if (context.getNumberBomber() == 1) {
                    selectedSkins[0] = selectedSkin;
                    context.setSkinType(new SkinType[]{selectedSkin});
                    log.info("Selected skin for single player: {}", selectedSkin.name());
                    ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.MAP_TYPE);
                }
                else {

                    selectedSkins[currentPlayerSelecting] = selectedSkin;
                    log.info("Player {} selected skin: {}", (currentPlayerSelecting + 1), selectedSkin.name());

                    if (currentPlayerSelecting == 0) {

                        currentPlayerSelecting = 1;
                    }
                    else {

                        context.setSkinType(selectedSkins);
                        log.info("Both players selected skins: P1={}, P2={}", selectedSkins[0].name(),
                                 selectedSkins[1].name());
                        ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.MAP_TYPE);

                        currentPlayerSelecting = 0;
                    }
                }
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
