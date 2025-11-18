package io.nghlong3004.game.manager;

import io.nghlong3004.assets.ObjectAssets;
import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.loader.AudioLoader;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.entities.Item;
import io.nghlong3004.model.type.ItemType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.TILE_SIZE;

@Slf4j
@RequiredArgsConstructor
public class ItemManager {
    @Getter
    private final List<Item> items;
    @Setter
    private ItemType[][] itemTypes;
    private final AudioLoader audioLoader;

    public void spawnItem(int gridX, int gridY) {
        if (itemTypes == null) {
            return;
        }
        int x = gridX - 1;
        int y = gridY - 5;
        if (itemTypes[x][y] != ItemType.BLANK) {
            Item item = new Item(gridX, gridY, itemTypes[x][y]);
            items.add(item);
            log.debug("Spawned {} at ({}, {})", itemTypes[x][y], gridX, gridY);
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
                    audioLoader.playEffect(AudioConstant.EAT_ITEM);
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
