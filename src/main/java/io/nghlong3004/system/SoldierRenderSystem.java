package io.nghlong3004.system;

import io.nghlong3004.model.Entity;
import io.nghlong3004.model.Soldier;
import io.nghlong3004.util.ObjectContainer;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class SoldierRenderSystem extends AbstractEntityRenderSystem {
    
    private final BufferedImage[][][] soldierSprites;
    
    public SoldierRenderSystem() {
        this.soldierSprites = ObjectContainer.getImageContainer().getSoldierSprites();
        log.debug("SoldierRenderSystem initialized with {} soldier types", soldierSprites.length);
    }
    
    @Override
    protected BufferedImage getSprite(Entity entity) {
        Soldier soldier = (Soldier) entity;
        int soldierType = soldier.getSoldierType() - 1;
        int direction = soldier.getDirection();
        return soldierSprites[soldierType][direction][0];
    }
    
    @Override
    protected Color getHitboxColor() {
        return Color.ORANGE;
    }
    
    @Override
    protected int getDeathLabelFontSize() {
        return 16;
    }
}
