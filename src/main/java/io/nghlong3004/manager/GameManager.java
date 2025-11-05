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
    private final BomberManager bomberManager;
    private final GameRender gameRender;

    public void play(MapType type, List<Bomber> bombers) {
        mapManager.setType(type);
        mapManager.loadMap();
        bomberManager.addAll(bombers);
    }


    public void reset() {
        bomberManager.reset();
    }

    public void update() {
        bomberManager.update();
    }

    public void render(Graphics g) {
        gameRender.render(g);
    }
}
