package io.nghlong3004.boom_battle_swing.view.scene;

import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.button.AbstractButton;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.event.MouseEvent;

@Getter
@AllArgsConstructor
public abstract class AbstractScene {
    protected final GameApplication game;

    public boolean isInButton(MouseEvent e, AbstractButton button) {
        return button.getHitbox().contains(e.getX(), e.getY());
    }
}
