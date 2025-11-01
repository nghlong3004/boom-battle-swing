package io.nghlong3004.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;

@Slf4j
@Getter
@Setter
public class Explosion {
    private int gridRow;
    private int gridCol;
    private float x;
    private float y;
    private int range;
    
    private int tick;
    private int index;
    private int maxFrames = 10;
    private int animationSpeed = 5;
    private boolean finished = false;
    
    private List<ExplosionTile> explosionTiles;
    
    public Explosion(int gridRow, int gridCol, float x, float y, int range) {
        this.gridRow = gridRow;
        this.gridCol = gridCol;
        this.x = x;
        this.y = y;
        this.range = range;
        this.tick = 0;
        this.index = 0;
        this.explosionTiles = new ArrayList<>();
        
        log.debug("Explosion created at grid position [row={}, col={}] with range={}", 
                 gridRow, gridCol, range);
    }
    
    public void update() {
        tick++;
        if (tick >= animationSpeed) {
            tick = 0;
            index++;
            if (index >= maxFrames) {
                finished = true;
                log.trace("Explosion animation finished at grid position [row={}, col={}]", gridRow, gridCol);
            }
        }
    }
    
    @Getter
    public static class ExplosionTile {
        private final int gridRow;
        private final int gridCol;
        private final float x;
        private final float y;
        private final Direction direction;
        private final boolean isEnd;
        
        public ExplosionTile(int gridRow, int gridCol, float x, float y, Direction direction, boolean isEnd) {
            this.gridRow = gridRow;
            this.gridCol = gridCol;
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.isEnd = isEnd;
        }
    }
    
    public enum Direction {
        CENTER, UP, DOWN, LEFT, RIGHT
    }
}
