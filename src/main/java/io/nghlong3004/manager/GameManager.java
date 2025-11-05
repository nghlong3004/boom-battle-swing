package io.nghlong3004.manager;

import io.nghlong3004.entity.Bomber;
import io.nghlong3004.type.MapType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GameManager {
    @Getter
    private final MapManager mapManager;
    @Getter
    private final BomberManager bomberManager;
    @Getter
    private final BombManager bombManager;
    private final GameRender gameRender;

    public void play(MapType type, List<Bomber> bombers) {
        mapManager.setType(type);
        mapManager.loadMap();
        setSpawnBombers(bombers);
        bomberManager.addAll(bombers);
    }

    private void setSpawnBombers(List<Bomber> bombers) {
        List<Point> points = mapManager.getSpawns(6);
        for (int i = 0; i < bombers.size(); ++i) {
            bombers.get(i)
                   .setX(points.get(i).x);
            bombers.get(i)
                   .setY(points.get(i).y);
            bombers.get(i)
                   .reset();
        }
    }

    public void reset() {
        bomberManager.reset();
        bombManager.reset();
    }

    public void update() {
        bomberManager.update();
        bombManager.update();
    }

    public void render(Graphics g) {
        gameRender.render(g);
    }
}
