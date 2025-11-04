package io.nghlong3004.context.state;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.menu.GameTypeComponent;
import io.nghlong3004.component.menu.MapTypeComponent;
import io.nghlong3004.component.menu.MenuTypeComponent;
import io.nghlong3004.component.menu.SkinTypeComponent;
import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.MenuType;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;
import static io.nghlong3004.constant.ImageConstant.BACKGROUND;

public class MainMenuState implements GameState {

    private final GameContext stateContext;
    private final Map<MenuType, GameComponent> gameComponentMap;
    @Setter
    private MenuType type;
    private BufferedImage background;

    public MainMenuState(GameContext stateContext) {
        this.stateContext = stateContext;
        gameComponentMap = new EnumMap<>(MenuType.class);
        loadMenuBackground();
        loadGameComponentMap();
    }

    private void loadMenuBackground() {
        background = ImageLoader.loadImage(BACKGROUND);
    }

    private void loadGameComponentMap() {
        gameComponentMap.put(MenuType.GAME_TYPE, new GameTypeComponent(stateContext));
        gameComponentMap.put(MenuType.MAP_TYPE, new MapTypeComponent(stateContext));
        gameComponentMap.put(MenuType.SKIN_TYPE, new SkinTypeComponent(stateContext));
        gameComponentMap.put(MenuType.MENU, new MenuTypeComponent(stateContext));
        type = MenuType.MENU;
    }

    @Override
    public void update() {
        gameComponentMap.get(type)
                        .update();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        gameComponentMap.get(type)
                        .render(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameComponentMap.get(type)
                        .mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void off() {
        if (stateContext.getState() != GameStateType.OPTION) {
            stateContext.getAudio()
                        .stopSong();
        }
    }

    @Override
    public void on() {
        stateContext.getAudio()
                    .playSong(AudioConstant.MENU);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseClicked(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gameComponentMap.get(type)
                        .mouseDragged(e);
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
