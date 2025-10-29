package io.nghlong3004.boom_battle_swing.model;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.IMAGE_BOMBER_WIDTH;

@Slf4j
public class Bomber extends Entity {

    private BufferedImage[][] animations;

    private float xDrawOffset = 10 * SCALE;
    private float yDrawOffset = 2 * SCALE;

    private int aniTick, aniIndex, aniSpeed = 15;
    private boolean moving = false;
    @Setter
    private boolean up, down, right, left;
    private float playerSpeed = 1.0f * SCALE;

    private int direction = DOWN;

    public Bomber(float x, float y, int width, int height) {
        super(x, y, width, height);
        importImage();
        setHitbox(x, y, (int) (IMAGE_BOMBER_WIDTH * SCALE - xDrawOffset),
                  (int) (IMAGE_BOMBER_HEIGHT * SCALE - yDrawOffset));
    }

    @Override
    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animations[direction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset),
                    width, height, null);
        drawHitbox(g);
    }

    public void setDirection(int direction) {
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
        int currentDirection = direction;
        if (moving) {
            if (left) {
                direction = LEFT;
            }
            if (right) {
                direction = RIGHT;
            }
            if (up) {
                direction = UP;
            }
            if (down) {
                direction = DOWN;
            }
        }
        else {
            aniIndex = 2;
        }
        if (currentDirection != direction) {
            aniIndex = 0;
            aniTick = 0;
        }
    }

    private void updatePos() {
        moving = false;
        if ((!left && !right && !up && !down) || ((left && right && up && down))) {
            return;
        }
        float xSpeed = 0;
        float ySpeed = 0;
        if (left) {
            xSpeed -= playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }
        if (up) {
            ySpeed -= playerSpeed;
        }
        if (down) {
            ySpeed += playerSpeed;
        }
        hitbox.x += xSpeed;
        hitbox.y += ySpeed;
        moving = true;
    }

    public void resetDirection() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    private void importImage() {
        animations = new BufferedImage[4][5];
        BufferedImage image = ImageUtil.loadImage("/images/player/boz.png");
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 5; ++j) {
                animations[i][j] = image.getSubimage(j * IMAGE_BOMBER_WIDTH, i * IMAGE_BOMBER_HEIGHT,
                                                     IMAGE_BOMBER_WIDTH, IMAGE_BOMBER_HEIGHT);
            }
        }


    }

}
