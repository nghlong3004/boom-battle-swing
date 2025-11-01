package io.nghlong3004.system;

import io.nghlong3004.model.Entity;
import io.nghlong3004.model.TileMap;
import io.nghlong3004.util.ObjectContainer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.AudioConstant.MOVE;
import static io.nghlong3004.constant.GameConstant.MOVE_SFX_COOLDOWN_MS;

public class GameSystem {
    private final UpdateSystem tileMapUpdater, entityUpdater;
    private final RenderSystem tileMapRenderer, entityRenderer;
    private final List<Entity> bombers;
    private final TileMap tileMap;
    private long lastMoveSfxAtMs = 0L;

    public GameSystem() {
        this.tileMapRenderer = new TileMapRenderSystem();
        this.tileMapUpdater = new TileMapUpdateSystem();
        this.entityRenderer = new EntityRenderSystem();
        this.entityUpdater = new EntityUpdateSystem();
        this.bombers = new ArrayList<>();
        this.tileMap = new TileMap();

    }

    public void update() {
        tileMapUpdater.update(tileMap);
        for (var bomber : bombers) {
            entityUpdater.update(bomber);
        }
    }

    public void render(Graphics g) {
        tileMapRenderer.render(g, tileMap);
        for (var bomber : bombers) {
            entityRenderer.render(g, bomber);
            if (bomber.isMoving()) {
                long now = System.currentTimeMillis();
                if (now - lastMoveSfxAtMs >= MOVE_SFX_COOLDOWN_MS) {
                    ObjectContainer.getAudioUtil().playEffect(MOVE);
                    lastMoveSfxAtMs = now;
                }
            }
        }
    }

    public void add(Entity bomber) {
        bombers.add(bomber);
    }

    public void removeAll() {
        bombers.clear();
    }

    public void resetAll() {
        for (var bomber : bombers) {
            bomber.reset();
        }
    }
}
