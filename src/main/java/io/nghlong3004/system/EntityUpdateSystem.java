package io.nghlong3004.system;


import io.nghlong3004.model.Entity;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.constant.BomberConstant.*;

@Slf4j
public class EntityUpdateSystem implements UpdateSystem {
    @Override
    public void update(Object object) {
        Entity entity = (Entity) object;
        updatePosition(entity);
        updateAnimationTick(entity);
        setAnimation(entity);
    }

    private void updateAnimationTick(Entity entity) {
        entity.setTick(entity.getTick() + 1);

        if (entity.getTick() >= ANIMATION_SPEED) {
            entity.setTick(0);
            entity.setIndex((entity.getIndex() + 1) % 5);
        }
    }

    private void setAnimation(Entity entity) {
        int currentDirection = entity.getDirection();

        if (entity.isMoving()) {
            if (entity.isLeft()) {
                entity.setDirection(LEFT);
            }
            else if (entity.isRight()) {
                entity.setDirection(RIGHT);
            }
            else if (entity.isUp()) {
                entity.setDirection(UP);
            }
            else if (entity.isDown()) {
                entity.setDirection(DOWN);
            }
        }
        else {
            entity.setIndex(2);
        }

        if (currentDirection != entity.getDirection()) {
            entity.setIndex(0);
            entity.setTick(0);
        }
    }

    private void updatePosition(Entity entity) {
        entity.setMoving(false);

        if (!entity.isLeft() && !entity.isRight() && !entity.isUp() && !entity.isDown()) {
            return;
        }

        float xSpeed = 0;
        float ySpeed = 0;

        if (entity.isLeft()) {
            xSpeed -= entity.getSpeed();
        }
        if (entity.isRight()) {
            xSpeed += entity.getSpeed();
        }
        if (entity.isUp()) {
            ySpeed -= entity.getSpeed();
        }
        if (entity.isDown()) {
            ySpeed += entity.getSpeed();
        }

        if (xSpeed != 0 && ySpeed != 0) {
            xSpeed *= DIAGONAL_SPEED_MODIFIER;
            ySpeed *= DIAGONAL_SPEED_MODIFIER;
        }

        Rectangle2D.Float hitbox = entity.getBox();
        hitbox.x += xSpeed;
        hitbox.y += ySpeed;
        entity.setMoving(true);
    }
}
