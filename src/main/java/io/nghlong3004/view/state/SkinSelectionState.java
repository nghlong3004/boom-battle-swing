package io.nghlong3004.view.state;

import io.nghlong3004.model.BomberSkin;
import io.nghlong3004.model.State;
import io.nghlong3004.system.GameStateContextHolder;
import io.nghlong3004.util.ImageLoaderUtil;
import io.nghlong3004.view.button.SkinButton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

@Slf4j
public class SkinSelectionState implements GameState {

    private SkinButton[] skinButtons;
    private BufferedImage background;
    private final StateContext stateContext;

    protected SkinSelectionState(StateContext stateContext) {
        this.stateContext = stateContext;
        loadBackground();
        loadSkinButtons();
    }

    private void loadBackground() {
        background = ImageLoaderUtil.loadImage(BACKGROUND);
    }

    private void loadSkinButtons() {
        BomberSkin[] skins = BomberSkin.values();
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
                BomberSkin selectedSkin = skinButton.getSkin();
                GameStateContextHolder.SKIN = selectedSkin;
                log.info("Selected skin: {}", selectedSkin.name);
                stateContext.changeState(State.MAP_SELECTION);
                break;
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

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateContext.changeState(State.MODE_SELECTION);
        }

        else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_4) {
            int index = e.getKeyCode() - KeyEvent.VK_1;
            if (index < skinButtons.length) {
                BomberSkin selectedSkin = BomberSkin.values()[index];
                GameStateContextHolder.SKIN = selectedSkin;
                log.info("Selected skin via keyboard: {}", selectedSkin.name);
                stateContext.changeState(State.MAP_SELECTION);
            }
        }
    }

    @Override
    public void exit() {
        log.info("Exiting skin selection screen");
    }

    @Override
    public void enter() {
        log.info("Entered skin selection screen");
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

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
