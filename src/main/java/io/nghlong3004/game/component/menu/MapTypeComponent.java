package io.nghlong3004.game.component.menu;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.MapButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.MainMenuState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.MapType;
import io.nghlong3004.model.type.MenuType;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

@Slf4j
public class MapTypeComponent extends GameComponent {
    private MapButton[] mapButtons;
    private BufferedImage background;

    public MapTypeComponent(GameContext stateContext) {
        super(stateContext);
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
            mapButtons[index] = new MapButton(x, y, buttonWidth, buttonHeight, mode, context.getAudio());
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
                context.setMapType(selectedMode);
                log.info("Selected map: {}", selectedMode.getAssetKey());
                ((MainMenuState) context.getGameState(GameStateType.MENU)).setType(MenuType.MENU);
                context.changeState(GameStateType.OFFLINE);
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

    private void reset() {
        for (var mapButton : mapButtons) {
            mapButton.reset();
        }
    }
}
