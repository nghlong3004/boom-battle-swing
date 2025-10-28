package io.nghlong3004.boom_battle_swing.view.scene;

import io.nghlong3004.boom_battle_swing.view.Game;
import io.nghlong3004.boom_battle_swing.view.scene.component.MenuButton;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.event.MouseEvent;

@Getter
@AllArgsConstructor
public abstract class AbstractScene {
    protected final Game game;

    public boolean isInMenuButton(MouseEvent e, MenuButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}
