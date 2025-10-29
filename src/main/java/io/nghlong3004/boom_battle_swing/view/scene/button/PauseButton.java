package io.nghlong3004.boom_battle_swing.view.scene.button;

import lombok.Data;

import java.awt.*;

@Data
public abstract class PauseButton {
    protected int x, y, width, height;
    protected Rectangle hitbox;

    public PauseButton(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        createHitbox();
    }

    private void createHitbox() {
        hitbox = new Rectangle(x, y, width, height);
    }
}
