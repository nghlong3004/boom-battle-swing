package io.nghlong3004.boom_battle_swing.model;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.boom_battle_swing.model.Action.IDLE;
import static io.nghlong3004.boom_battle_swing.model.Action.RUNNING;
import static io.nghlong3004.boom_battle_swing.model.Direction.DOWN;

public class Bomber extends Entity {

    private List<List<BufferedImage>> images;

    private int aniTick, aniIndex, aniSpeed = 15;
    private boolean moving = false;

    private Action action = IDLE;

    private Direction direction = DOWN;

    public Bomber(float x, float y) {
        super(x, y);
        importImage();
    }

    @Override
    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePos();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(images.get(1).get(0), (int) x, (int) y, null);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        moving = true;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 5) {
                aniIndex = 0;
            }
        }

    }

    private void setAnimation() {
        if (moving) {
            action = RUNNING;
        }
        else {
            action = IDLE;
        }
    }

    private void updatePos() {
        if (moving) {
            switch (direction) {
                case LEFT -> x -= 5;
                case UP -> y -= 5;
                case RIGHT -> x += 5;
                case DOWN -> y += 5;
            }
        }
    }

    private void importImage() {
        images = new ArrayList<>();
        for (var direction : Direction.values()) {
            var playerImage = new ArrayList<BufferedImage>();
            for (int i = 1; i <= 5; ++i) {
                String name = "/images/player/player_%s_%d.png".formatted(direction.getMean(), i);
                playerImage.add(ImageUtil.loadImage(name));
            }
            images.add(playerImage);
        }
    }

}
