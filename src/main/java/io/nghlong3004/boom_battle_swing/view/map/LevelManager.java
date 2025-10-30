package io.nghlong3004.boom_battle_swing.view.map;

import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.GameApplication;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.GameConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.LEVEL_ATLAS;

public class LevelManager {
    private GameApplication game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManager(GameApplication game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(ImageUtil.importLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = ImageUtil.loadImage(LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g) {
        for (int j = 0; j < TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < TILES_IN_WIDTH; i++) {
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], TILES_SIZE * i, TILES_SIZE * j, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public int[][] getLevelData() {
        return levelOne.getLvlData();
    }
}
