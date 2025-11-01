package io.nghlong3004.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.Rectangle2D;

import static io.nghlong3004.constant.GameConstant.TILES_SIZE;

@Slf4j
@Getter
@Setter
public class Bomb {
    private float x;
    private float y;
    private int gridRow;
    private int gridCol;
    private int width;
    private int height;
    private Rectangle2D.Float box;
    
    private int tick;
    private int index;
    
    private int explosionRange = 2;
    private long placedTime;
    private long fuseTime = 3000;
    private boolean exploded = false;
    private boolean allowEntityExit = true;
    
    public Bomb(float bomberX, float bomberY, int xDrawOffset, int yDrawOffset) {
        float relativeX = bomberX - xDrawOffset;
        float relativeY = bomberY - yDrawOffset;
        
        this.gridCol = (int) (relativeX / TILES_SIZE);
        this.gridRow = (int) (relativeY / TILES_SIZE);
        
        if (this.gridCol < 0) this.gridCol = 0;
        if (this.gridRow < 0) this.gridRow = 0;
        
        float gridX = gridCol * TILES_SIZE + xDrawOffset;
        float gridY = gridRow * TILES_SIZE + yDrawOffset;
        
        this.width = 35;
        this.height = 33;
        
        float centerOffsetX = (TILES_SIZE - width) / 2f;
        float centerOffsetY = (TILES_SIZE - height) / 2f;
        
        this.x = gridX + centerOffsetX;
        this.y = gridY + centerOffsetY;
        
        this.box = new Rectangle2D.Float(this.x, this.y, width, height);
        this.placedTime = System.currentTimeMillis();
        this.tick = 0;
        this.index = 0;
        
        log.debug("Bomb placed at grid position [row={}, col={}], screen position [x={}, y={}]", 
                 gridRow, gridCol, x, y);
    }
    
    public boolean shouldExplode() {
        return System.currentTimeMillis() - placedTime >= fuseTime && !exploded;
    }
    
    public void explode() {
        this.exploded = true;
        log.debug("Bomb exploded at grid position [row={}, col={}]", gridRow, gridCol);
    }
    
    public boolean isSameGridPosition(int row, int col) {
        return this.gridRow == row && this.gridCol == col;
    }
}
