package io.nghlong3004.context.state;

import io.nghlong3004.component.button.MapButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.GameStateContext;
import io.nghlong3004.entity.MapType;
import io.nghlong3004.loader.ImageLoader;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

@Slf4j
public class MapSelectionState implements GameState {
    private MapButton[] mapButtons;
    private BufferedImage background;
    private final GameContext stateContext;

    public MapSelectionState(GameContext stateContext) {
        this.stateContext = stateContext;
        loadBackground();
        loadMapButtons();
    }

    private void loadBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadMapButtons() {

        MapType[] allModes = MapType.values();
        int validModeCount = allModes.length;
        mapButtons = new MapButton[validModeCount];

        int buttonWidth = (int) (120 * SCALE);
        int buttonHeight = (int) (140 * SCALE);
        int spacing = (int) (15 * SCALE);


        int totalWidth = (buttonWidth * validModeCount) + (spacing * (validModeCount - 1));
        int startX = (GAME_WIDTH - totalWidth) / 2 + buttonWidth / 2;
        int y = GAME_HEIGHT / 2;

        int index = 0;
        for (MapType mode : allModes) {
            int x = startX + index * (buttonWidth + spacing);
            mapButtons[index] = new MapButton(x, y, buttonWidth, buttonHeight, mode);
            index++;
        }
    }

    @Override
    public void update() {
        for (var mapButton : mapButtons) {
            mapButton.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        for (MapButton button : mapButtons) {
            button.render(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var mapButton : mapButtons) {
            if (mapButton.isMouseOver(e)) {
                mapButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (var mapButton : mapButtons) {
            if (mapButton.isMouseOver(e) && mapButton.isMousePressed()) {
                MapType selectedMode = mapButton.getTileMode();
                GameStateContext.MAP_TYPE = selectedMode;
                log.info("Selected map: {}", selectedMode.getAssetKey());
                stateContext.changeState(GameStateID.PLAYING);
                break;
            }
        }
        reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var mapButton : mapButtons) {
            mapButton.setMouseOver(false);
        }
        for (var mapButton : mapButtons) {
            if (mapButton.isMouseOver(e)) {
                mapButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stateContext.changeState(GameStateID.SKIN_SELECTION);
        }

        else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_5) {
            int index = e.getKeyCode() - KeyEvent.VK_1;
            if (index < mapButtons.length) {
                MapType selectedMode = mapButtons[index].getTileMode();
                GameStateContext.MAP_TYPE = selectedMode;
                log.info("Selected map via keyboard: {}", selectedMode.getAssetKey());
                stateContext.changeState(GameStateID.PLAYING);
            }
        }
    }

    @Override
    public void off() {
        log.info("Exiting map selection screen");
    }

    @Override
    public void on() {
        log.info("Entered map selection screen");
    }

    private void reset() {
        for (var mapButton : mapButtons) {
            mapButton.reset();
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
