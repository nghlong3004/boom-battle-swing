package io.nghlong3004.system;

import io.nghlong3004.model.Soldier;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import static io.nghlong3004.constant.BomberConstant.*;
import static io.nghlong3004.constant.SoldierConstant.AI_UPDATE_INTERVAL;
import static io.nghlong3004.constant.SoldierConstant.DIRECTION_CHANGE_COOLDOWN;

@Slf4j
public class SoldierAISystem implements UpdateSystem {
    
    private final Random random = new Random();
    
    @Override
    public void update(Object object) {
        Soldier soldier = (Soldier) object;
        
        if (!soldier.isAlive()) {
            soldier.setMoving(false);
            soldier.resetDirection();
            return;
        }
        
        soldier.setAiTick(soldier.getAiTick() + 1);
        
        if (soldier.getAiTick() >= AI_UPDATE_INTERVAL) {
            soldier.setAiTick(0);
            updateAI(soldier);
        }
    }
    
    private void updateAI(Soldier soldier) {
        if (soldier.getDirectionChangeCooldown() > 0) {
            soldier.setDirectionChangeCooldown(soldier.getDirectionChangeCooldown() - AI_UPDATE_INTERVAL);
            return;
        }
        
        if (random.nextFloat() < 0.05f) {
            changeDirection(soldier);
            soldier.setDirectionChangeCooldown(DIRECTION_CHANGE_COOLDOWN);
        }
    }
    
    private void changeDirection(Soldier soldier) {
        soldier.resetDirection();
        
        int direction = random.nextInt(5);
        
        switch (direction) {
            case 0 -> {
                soldier.setLeft(true);
                soldier.setDirection(LEFT);
            }
            case 1 -> {
                soldier.setRight(true);
                soldier.setDirection(RIGHT);
            }
            case 2 -> {
                soldier.setUp(true);
                soldier.setDirection(UP);
            }
            case 3 -> {
                soldier.setDown(true);
                soldier.setDirection(DOWN);
            }
            case 4 -> {
                soldier.resetDirection();
            }
        }
        
        log.trace("Soldier changed direction to: {}", direction);
    }
}
