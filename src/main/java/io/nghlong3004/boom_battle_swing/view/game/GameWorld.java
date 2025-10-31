package io.nghlong3004.boom_battle_swing.view.game;

import io.nghlong3004.boom_battle_swing.model.Bomber;
import io.nghlong3004.boom_battle_swing.model.TileMap;
import io.nghlong3004.boom_battle_swing.view.render.BomberRenderer;
import io.nghlong3004.boom_battle_swing.view.render.Renderer;
import io.nghlong3004.boom_battle_swing.view.render.TileMapRenderer;
import io.nghlong3004.boom_battle_swing.view.update.BomberUpdater;
import io.nghlong3004.boom_battle_swing.view.update.TileMapUpdater;
import io.nghlong3004.boom_battle_swing.view.update.Updater;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.MOVE;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.MOVE_SFX_COOLDOWN_MS;
import static io.nghlong3004.boom_battle_swing.util.ObjectContainer.getAudioPlayer;

public class GameWorld {
    private final Updater tileMapUpdater, bomberUpdater;
    private final Renderer tileMapRenderer, bomberRenderer;
    private final List<Bomber> bombers;
    private final TileMap tileMap;
    private long lastMoveSfxAtMs = 0L;

    public GameWorld() {
        this.tileMapRenderer = new TileMapRenderer();
        this.tileMapUpdater = new TileMapUpdater();
        this.bomberRenderer = new BomberRenderer();
        this.bomberUpdater = new BomberUpdater();
        this.bombers = new ArrayList<>();
        this.tileMap = new TileMap();
    }

    public void update() {
        tileMapUpdater.update(tileMap);
        for (var bomber : bombers) {
            bomberUpdater.update(bomber);
        }
    }

    public void render(Graphics g) {
        tileMapRenderer.render(g, tileMap);
        for (var bomber : bombers) {
            bomberRenderer.render(g, bomber);
            if (bomber.isMoving()) {
                long now = System.currentTimeMillis();
                if (now - lastMoveSfxAtMs >= MOVE_SFX_COOLDOWN_MS) {
                    getAudioPlayer().playEffect(MOVE);
                    lastMoveSfxAtMs = now;
                }
            }
        }
    }

    public void add(Bomber bomber) {
        bombers.add(bomber);
    }

    public void removeAll() {
        bombers.clear();
    }

    public void resetAll() {
        for (var bomber : bombers) {
            bomber.resetAll();
        }
    }

}
