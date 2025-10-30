package io.nghlong3004.boom_battle_swing.view.scene;

import io.nghlong3004.boom_battle_swing.view.scene.component.OptionComponent;
import io.nghlong3004.boom_battle_swing.view.scene.component.PlayingComponent;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Consumer;

public final class SceneRouter {
    private final GameScene menu;
    @Getter
    private final PlayingComponent playing;
    private final OptionComponent option;

    private GameState last = GameState.state;
    @Setter
    private Consumer<GameState> onStateChanged;

    public SceneRouter(GameScene menu, PlayingComponent playing, OptionComponent option) {
        this.menu = menu;
        this.playing = playing;
        this.option = option;
    }

    public void update() {
        handleStateTransition();
        switch (GameState.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case OPTION -> option.update();
            case QUIT -> System.exit(0);
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTION -> option.draw(g);
        }
    }

    private void handleStateTransition() {
        GameState current = GameState.state;
        if (current == last) {
            return;
        }

        GameState prev = last;
        last = current;

        if (prev == GameState.OPTION || current == GameState.OPTION) {
            return;
        }

        if (onStateChanged != null) {
            onStateChanged.accept(current);
        }
    }
}
