package io.nghlong3004.game.manager;

import io.nghlong3004.assets.ObjectAssets;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.entities.Item;
import io.nghlong3004.model.type.ItemType;
import io.nghlong3004.util.AudioHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
public class ItemManager {
    @Getter
    private final List<Item> items;

    public ItemManager() {
        this.items = new ArrayList<>();
    }

    public void spawnItem(int gridX, int gridY) {
        if (Math.random() < 0.6) {
            ItemType randomType = ItemType.random();
            Item item = new Item(gridX, gridY, randomType);
            items.add(item);
            log.debug("Spawned {} at ({}, {})", randomType, gridX, gridY);
        }
    }

    public void destroyItemAt(int gridX, int gridY) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getGridX() == gridX && item.getGridY() == gridY) {
                iterator.remove();
                log.debug("Item destroyed at ({}, {})", gridX, gridY);
            }
        }
    }

    public void checkCollisions(List<Bomber> bombers) {
        if (bombers == null || bombers.isEmpty()) {
            return;
        }
        for (Bomber bomber : bombers) {
            if (!bomber.isAlive()) {
                continue;
            }

            int bomberGridX = Math.round(bomber.getBox().y / TILE_SIZE);
            int bomberGridY = Math.round(bomber.getBox().x / TILE_SIZE);

            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item.getGridX() == bomberGridX && item.getGridY() == bomberGridY) {
                    item.applyEffect(bomber);
                    iterator.remove();
                    AudioHelper.playEatItemSound();
                    log.info("Bomber collected {}", item.getType());
                }
            }
        }
    }

    public void update() {
        for (Item item : items) {
            item.update();
        }
    }

    public void render(Graphics g) {
        BufferedImage[] itemSprites = ObjectAssets.getInstance()
                                                  .getItemAssets();
        BufferedImage[] effectSprites = ObjectAssets.getInstance()
                                                    .getItemEffectFramesAssets();

        if (itemSprites == null || itemSprites.length == 0) {
            return;
        }

        int itemSize = (int) (TILE_SIZE * 0.7f);
        int offset = (TILE_SIZE - itemSize) / 2;

        for (Item item : items) {
            if (!item.isCollected()) {
                int x = item.getPixelX() + offset;
                int y = item.getPixelY() + offset;

                if (effectSprites != null && effectSprites.length > 0) {
                    BufferedImage effect = effectSprites[item.getAnimationFrame()];
                    if (effect != null) {
                        g.drawImage(effect, item.getPixelX(), item.getPixelY(), TILE_SIZE, TILE_SIZE, null);
                    }
                }

                BufferedImage sprite = itemSprites[item.getType().id];
                if (sprite != null) {
                    g.drawImage(sprite, x, y, itemSize, itemSize, null);
                }
            }
        }
    }

    public void reset() {
        items.clear();
    }
}
