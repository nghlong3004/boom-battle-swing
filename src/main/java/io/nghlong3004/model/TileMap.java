package io.nghlong3004.model;

import io.nghlong3004.util.CollisionUtil;
import io.nghlong3004.util.FileLoaderUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.*;

@Slf4j
@Getter
@Setter
public class TileMap {
    private TileMode mode;
    private int[][] data;

    private int xDrawOffSet;
    private int yDrawOffSet;
    
    private List<SpawnPoint> spawnPoints;

    public void importDataMap(String name) {
        data = FileLoaderUtil.loadFileMapData(name);
        xDrawOffSet = (GAME_WIDTH - TILES_SIZE * data[0].length) >>> 1;
        yDrawOffSet = (GAME_HEIGHT - TILES_SIZE * data.length) >>> 1;
        
        extractSpawnPoints();
    }
    
    private void extractSpawnPoints() {
        spawnPoints = new ArrayList<>();
        
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                int tileValue = data[row][col];
                
                if (CollisionUtil.isSpawnTile(tileValue)) {
                    float x = col * TILES_SIZE + xDrawOffSet;
                    float y = row * TILES_SIZE + yDrawOffSet;
                    
                    spawnPoints.add(new SpawnPoint(row, col, x, y, tileValue));
                    
                    data[row][col] = 1;
                    
                    log.debug("Found spawn point: type={}, grid=[{},{}], pos=[{},{}]", 
                             tileValue, row, col, x, y);
                }
            }
        }
        
        log.info("Extracted {} spawn points from map", spawnPoints.size());
    }
    
    public SpawnPoint getPlayerSpawn() {
        return spawnPoints.stream()
                         .filter(sp -> sp.getSpawnType() == 6)
                         .findFirst()
                         .orElse(null);
    }
    
    public List<SpawnPoint> getSoldierSpawns() {
        return spawnPoints.stream()
                         .filter(sp -> sp.getSpawnType() >= 7 && sp.getSpawnType() <= 9)
                         .toList();
    }
}
