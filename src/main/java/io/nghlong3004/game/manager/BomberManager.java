package io.nghlong3004.game.manager;

import io.nghlong3004.assets.BomberAssets;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.entities.Entity;
import io.nghlong3004.util.CollisionChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.BomberConstant.ANIMATION_SPEED;
import static io.nghlong3004.constant.EntityConstant.*;
import static io.nghlong3004.constant.GameConstant.SCALE;
import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
public class BomberManager {

    @Getter
    private final List<Bomber> bombers;
    @Setter
    private BombManager bombManager;
    @Getter
    @Setter
    private CollisionChecker collisionChecker;

    public BomberManager() {
        this.bombers = new ArrayList<>();
    }

    public void addAll(List<Bomber> bombers) {
        this.bombers.addAll(bombers);
    }

    public void add(Object object) {
        bombers.add((Bomber) object);
    }

    public void render(Graphics g) {
        for (var bomber : bombers) {
            if (!bomber.isAlive() && !bomber.isDying()) {
                continue;
            }
            renderBomber(g, bomber);
        }
    }

    public void renderBomber(Graphics g, Bomber bomber) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Float hitbox = bomber.getBox();
        BufferedImage sprite = getBufferedImage(bomber);
        if (sprite != null) {
            int spriteX = (int) bomber.getX();
            int spriteY = (int) bomber.getY();
            int width = (int) bomber.getWidth();
            int height = (int) bomber.getHeight();

            if (bomber.isDying()) {
                width = (int) (TILE_SIZE * SCALE);
                height = (int) (TILE_SIZE * SCALE);
                int offsetX = ((int) bomber.getWidth() - width) / 2;
                int offsetY = ((int) bomber.getHeight() - height) / 2;
                spriteX += offsetX;
                spriteY += offsetY;
            }

            g2d.drawImage(sprite, spriteX, spriteY, width, height, null);
        }
        // drawHitbox(g, hitbox);
    }

    public void update() {
        for (var bomber : bombers) {
            if (bomber.isDying()) {
                bomber.updateDeathAnimation();
                continue;
            }

            if (!bomber.isAlive()) {
                continue;
            }

            if (bomber.isPlaceBombRequested() && bombManager != null) {
                bombManager.placeBomb(bomber);
                bomber.setPlaceBombRequested(false);
            }

            updatePosition(bomber);
            updateAnimationTick(bomber);
            setAnimation(bomber);
        }
    }

    protected void updateAnimationTick(Entity entity) {
        entity.setTick(entity.getTick() + 1);

        if (entity.getTick() >= ANIMATION_SPEED) {
            entity.setTick(0);
            entity.setIndex((entity.getIndex() + 1) % 5);
        }
    }

    protected void setAnimation(Entity entity) {
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

    protected void updatePosition(Entity entity) {
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
        entity.setMoving(true);

        Rectangle2D.Float hitbox = entity.getBox();
        float newX = hitbox.x + xSpeed;
        float newY = hitbox.y + ySpeed;
        Rectangle2D.Float newHitbox = new Rectangle2D.Float(newX, newY, hitbox.width, hitbox.height);

        if (collisionChecker != null && !collisionChecker.canMoveTo(newHitbox, (Bomber) entity)) {
            if (xSpeed != 0 && ySpeed != 0) {
                Rectangle2D.Float xOnlyHitbox = new Rectangle2D.Float(newX, hitbox.y, hitbox.width, hitbox.height);
                if (collisionChecker.canMoveTo(xOnlyHitbox, (Bomber) entity)) {
                    ySpeed = 0;
                }
                else {
                    Rectangle2D.Float yOnlyHitbox = new Rectangle2D.Float(hitbox.x, newY, hitbox.width, hitbox.height);
                    if (collisionChecker.canMoveTo(yOnlyHitbox, (Bomber) entity)) {
                        xSpeed = 0;
                    }
                    else {
                        log.debug("Collision detected at position ({}, {})", newX, newY);
                        return;
                    }
                }
            }
            else {
                return;
            }
        }

        entity.setX(entity.getX() + xSpeed);
        entity.setY(entity.getY() + ySpeed);

        hitbox.x = entity.getX() + HITBOX_OFFSET_X;
        hitbox.y = entity.getY() + HITBOX_OFFSET_Y;
    }

    private BufferedImage getBufferedImage(Bomber bomber) {
        if (bomber.isDying()) {
            BufferedImage[] deathSprites = BomberAssets.getInstance()
                                                       .getBomberDeathAssets();
            if (deathSprites != null && deathSprites.length > 0) {
                int frameIndex = Math.min(bomber.getDeathAnimationFrame(), deathSprites.length - 1);
                return deathSprites[frameIndex];
            }
        }

        int direction = bomber.getDirection();
        int index = bomber.getIndex();
        return BomberAssets.getInstance()
                           .getBomberAssets()
                           .get(bomber.getSkinId())[direction][index];
    }

    protected void drawHitbox(Graphics g, Rectangle2D.Float hitbox) {
        g.setColor(Color.black);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void reset() {
        bombers.clear();
    }
}
