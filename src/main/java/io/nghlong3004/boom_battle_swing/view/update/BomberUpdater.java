package io.nghlong3004.boom_battle_swing.view.update;

import io.nghlong3004.boom_battle_swing.model.Bomber;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.*;

public class BomberUpdater implements Updater {
    @Override
    public void update(Object entity) {
        Bomber bomber = (Bomber) entity;
        updatePosition(bomber);
        updateAnimationTick(bomber);
        setAnimation(bomber);
    }

    private void updateAnimationTick(Bomber bomber) {
        bomber.setAnimationTick(bomber.getAnimationTick() + 1);

        if (bomber.getAnimationTick() >= bomber.getAnimationSpeed()) {
            bomber.setAnimationTick(0);
            bomber.setAnimationIndex(bomber.getAnimationIndex() + 1);

            if (bomber.getAnimationIndex() >= 5) {
                bomber.setAnimationIndex(0);
            }
        }
    }

    private void setAnimation(Bomber bomber) {
        int currentDirection = bomber.getDirection();

        if (bomber.isMoving()) {
            if (bomber.isLeft()) {
                bomber.setDirection(LEFT);
            }
            else if (bomber.isRight()) {
                bomber.setDirection(RIGHT);
            }
            else if (bomber.isUp()) {
                bomber.setDirection(UP);
            }
            else if (bomber.isDown()) {
                bomber.setDirection(DOWN);
            }
        }
        else {
            bomber.setAnimationIndex(2);
        }

        if (currentDirection != bomber.getDirection()) {
            bomber.setAnimationIndex(0);
            bomber.setAnimationTick(0);
        }
    }

    private void updatePosition(Bomber bomber) {
        bomber.setMoving(false);

        if (!bomber.isLeft() && !bomber.isRight() && !bomber.isUp() && !bomber.isDown()) {
            return;
        }

        float xSpeed = 0;
        float ySpeed = 0;

        if (bomber.isLeft()) {
            xSpeed -= bomber.getBomberSpeed();
        }
        if (bomber.isRight()) {
            xSpeed += bomber.getBomberSpeed();
        }
        if (bomber.isUp()) {
            ySpeed -= bomber.getBomberSpeed();
        }
        if (bomber.isDown()) {
            ySpeed += bomber.getBomberSpeed();
        }

        if (xSpeed != 0 && ySpeed != 0) {
            xSpeed *= DIAGONAL_SPEED_MODIFIER;
            ySpeed *= DIAGONAL_SPEED_MODIFIER;
        }

        Rectangle2D.Float hitbox = bomber.getHitbox();
        hitbox.x += xSpeed;
        hitbox.y += ySpeed;
        bomber.setMoving(true);
    }
}
