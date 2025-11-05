package io.nghlong3004.manager;

import io.nghlong3004.assets.BomberAssets;
import io.nghlong3004.entity.Bomber;
import io.nghlong3004.entity.Entity;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.BomberConstant.ANIMATION_SPEED;
import static io.nghlong3004.constant.EntityConstant.*;

@Slf4j
public class BomberManager {

    private final List<Bomber> bombers;
    private BombManager bombManager; // Will be set later

    public BomberManager() {
        this.bombers = new ArrayList<>();
    }

    public void setBombManager(BombManager bombManager) {
        this.bombManager = bombManager;
    }

    public void addAll(List<Bomber> bombers) {
        this.bombers.addAll(bombers);
    }

    public void add(Object object) {
        bombers.add((Bomber) object);
    }

    public void render(Graphics g) {
        for (var bomber : bombers) {
            Graphics2D g2d = (Graphics2D) g;
            Rectangle2D.Float hitbox = bomber.getBox();
            BufferedImage sprite = getBufferedImage(bomber);
            if (sprite != null) {
                g2d.drawImage(sprite, (int) hitbox.getX() - 8, (int) hitbox.getY() - 30, (int) bomber.getWidth(),
                              (int) bomber.getHeight(), null);
            }
            drawHitbox(g, hitbox);
        }
    }

    public void update() {
        for (var bomber : bombers) {
            // Handle bomb placement request
            if (bomber.isPlaceBombRequested() && bombManager != null) {
                bombManager.placeBomb(bomber);
                bomber.setPlaceBombRequested(false); // Reset the request
            }
            
            updatePosition(bomber);
            updateAnimationTick(bomber);
            setAnimation(bomber);
        }
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

        Rectangle2D.Float newHitbox = new Rectangle2D.Float(newX, newY, hitbox.width, hitbox.height);
        hitbox.x = newX;
        hitbox.y = newY;
        entity.setMoving(true);

    }

    private BufferedImage getBufferedImage(Bomber bomber) {
        int direction = bomber.getDirection();
        int index = bomber.getIndex();
        return BomberAssets.getInstance()
                           .getBomberAssets()
                           .get(bomber.getSkin().id)[direction][index];
    }

    protected void drawHitbox(Graphics g, Rectangle2D.Float hitbox) {
        g.setColor(Color.black);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void reset() {
        bombers.clear();
    }
}
