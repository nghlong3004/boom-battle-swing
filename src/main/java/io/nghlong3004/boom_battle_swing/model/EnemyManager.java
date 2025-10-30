package io.nghlong3004.boom_battle_swing.model;

import io.nghlong3004.boom_battle_swing.constant.GameConstant;
import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.view.scene.component.PlayingComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.*;

public class EnemyManager {
    private PlayingComponent playing;
    private BufferedImage[][] crabbyArr;
    private List<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(PlayingComponent playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = GetCrabs();
        System.out.println("size of crabs: " + crabbies.size());
    }

    public void update() {
        for (Crabby c : crabbies) {
            c.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset,
                        (int) c.getHitbox().y, CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }

    }

    public static ArrayList<Crabby> GetCrabs() {
        BufferedImage img = GetSpriteAtlas(ImageConstant.LEVEL_ONE_DATA);
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (i == 3 && j == 3) {
                    list.add(new Crabby(i * GameConstant.TILES_SIZE, j * GameConstant.TILES_SIZE));
                }
                if (value == CRABBY) {
                    list.add(new Crabby(i * GameConstant.TILES_SIZE, j * GameConstant.TILES_SIZE));
                }
            }
        }
        return list;

    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = GetSpriteAtlas(ImageConstant.CRABBY);
        for (int j = 0; j < crabbyArr.length; j++) {
            for (int i = 0; i < crabbyArr[j].length; i++) {
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT,
                                                   CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = EnemyManager.class.getResourceAsStream(fileName);
        try {
            assert is != null;
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
}
