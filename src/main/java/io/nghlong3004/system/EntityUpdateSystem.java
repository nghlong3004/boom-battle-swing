package io.nghlong3004.system;


import io.nghlong3004.model.Bomb;
import io.nghlong3004.model.Entity;
import io.nghlong3004.model.TileMap;
import io.nghlong3004.util.CollisionUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static io.nghlong3004.constant.BomberConstant.*;

@Slf4j
public class EntityUpdateSystem implements UpdateSystem {

    @Setter
    private TileMap tileMap;
    
    @Setter
    private List<Bomb> bombs;
    
    @Setter
    private List<Entity> allEntities;

    @Override
    public void update(Object object) {
        Entity entity = (Entity) object;

        if (!entity.isAlive()) {
            if (entity.isMoving()) {
                entity.setMoving(false);
            }
            return;
        }

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
        float newX = hitbox.x + xSpeed;
        float newY = hitbox.y + ySpeed;

        if (tileMap != null && CollisionUtil.canMove(hitbox, newX, newY, tileMap)) {
            Rectangle2D.Float newHitbox = new Rectangle2D.Float(newX, newY, hitbox.width, hitbox.height);
            
            if (!collidesWithBombs(newHitbox)) {
                if (!collidesWithOtherEntities(entity, newHitbox)) {
                    hitbox.x = newX;
                    hitbox.y = newY;
                    entity.setMoving(true);
                }
            }
        }
    }
    
    private boolean collidesWithBombs(Rectangle2D.Float hitbox) {
        if (bombs == null) {
            return false;
        }
        
        for (Bomb bomb : bombs) {
            if (bomb.isExploded()) {
                continue;
            }
            
            if (hitbox.intersects(bomb.getBox())) {
                if (bomb.isAllowEntityExit()) {
                    return false;
                }
                return true;
            }
        }
        
        return false;
    }
    
    private boolean collidesWithOtherEntities(Entity currentEntity, Rectangle2D.Float hitbox) {
        if (allEntities == null) {
            return false;
        }
        
        for (Entity other : allEntities) {
            if (other == currentEntity || !other.isAlive()) {
                continue;
            }
            
            if (hitbox.intersects(other.getBox())) {
                return true;
            }
        }
        
        return false;
    }
}
