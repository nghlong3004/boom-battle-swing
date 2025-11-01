package io.nghlong3004.system;

import io.nghlong3004.model.*;
import io.nghlong3004.util.CollisionUtil;
import io.nghlong3004.util.ObjectContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.nghlong3004.constant.AudioConstant.*;
import static io.nghlong3004.constant.GameConstant.MOVE_SFX_COOLDOWN_MS;

@Slf4j
public class GameSystem {
    private final UpdateSystem tileMapUpdater, bombUpdater;
    private final BomberAISystem bomberAISystem;
    private final EntityUpdateSystem entityUpdater;
    private final RenderSystem tileMapRenderer, entityRenderer, bombRenderer, explosionRenderer, itemRenderer;
    private final List<Bomber> bombers;
    private final List<Bomb> bombs;
    private final List<Explosion> explosions;
    private final List<Item> items;
    @Getter
    private final TileMap tileMap;
    private long lastMoveSfxAtMs = 0L;
    @Setter
    private boolean silentMode = false;

    public GameSystem() {
        this.tileMapRenderer = new TileMapRenderSystem();
        this.tileMapUpdater = new TileMapUpdateSystem();
        this.entityRenderer = new EntityRenderSystem();
        this.entityUpdater = new EntityUpdateSystem();
        this.bombRenderer = new BombRenderSystem();
        this.bombUpdater = new BombUpdateSystem();
        this.explosionRenderer = new ExplosionRenderSystem();
        this.itemRenderer = new ItemRenderSystem();
        this.bomberAISystem = new BomberAISystem();
        this.bombers = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.items = new ArrayList<>();
        this.tileMap = new TileMap();

        this.entityUpdater.setTileMap(tileMap);
        this.entityUpdater.setBombs(bombs);
        log.info("GameSystem initialized successfully");
    }

    public void update() {
        tileMapUpdater.update(tileMap);

        List<Bomber> bombersCopy = new ArrayList<>(bombers);

        List<Entity> allEntities = new ArrayList<>(bombersCopy);
        entityUpdater.setAllEntities(allEntities);

        bomberAISystem.setAllBombers(bombersCopy);
        bomberAISystem.setBombs(bombs);
        bomberAISystem.setExplosions(explosions);
        bomberAISystem.setTileMap(tileMap);

        List<Bomber> deadBombersToRemove = new ArrayList<>();

        for (var bomber : bombersCopy) {

            if (bomber.isDead()) {
                bomber.updateDeathAnimation();
                continue;
            }

            if (!bomber.isPlayer()) {
                bomberAISystem.update(bomber);
            }

            if (bomber.isPlaceBombRequested()) {
                placeBomb(bomber);
                bomber.setPlaceBombRequested(false);
            }

            entityUpdater.update(bomber);


            checkWalkOverDeadBomber(bomber, bombersCopy, deadBombersToRemove);
        }

        bombers.removeAll(deadBombersToRemove);
        checkBombExitStatus();
        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            bombUpdater.update(bomb);
            if (bomb.isExploded()) {
                createExplosion(bomb);
                List<Bomber> bombersCopyForBomb = new ArrayList<>(bombers);
                for (var bomber : bombersCopyForBomb) {
                    bomber.decrementBombCount();
                }
                bombIterator.remove();
            }
        }

        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            explosion.update();
            checkExplosionDamage(explosion);
            if (explosion.isFinished()) {
                explosionIterator.remove();
            }
        }
        Iterator<Item> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            item.update();
            checkItemCollection(item);
            if (item.isCollected()) {
                itemIterator.remove();
            }
        }
    }

    public void render(Graphics g) {
        tileMapRenderer.render(g, tileMap);
        List<Explosion> explosionsCopy = new ArrayList<>(explosions);
        for (var explosion : explosionsCopy) {
            explosionRenderer.render(g, explosion);
        }
        List<Bomb> bombsCopy = new ArrayList<>(bombs);
        for (var bomb : bombsCopy) {
            bombRenderer.render(g, bomb);
        }
        List<Item> itemsCopy = new ArrayList<>(items);
        for (var item : itemsCopy) {
            itemRenderer.render(g, item);
        }
        List<Bomber> bombersCopy = new ArrayList<>(bombers);
        for (var bomber : bombersCopy) {
            entityRenderer.render(g, bomber);

            if (!silentMode && bomber.isPlayer() && bomber.isMoving()) {
                long now = System.currentTimeMillis();
                if (now - lastMoveSfxAtMs >= MOVE_SFX_COOLDOWN_MS) {
                    ObjectContainer.getAudioUtil().playEffect(MOVE);
                    lastMoveSfxAtMs = now;
                }
            }
        }
    }

    private void createExplosion(Bomb bomb) {
        Explosion explosion = new Explosion(bomb.getGridRow(), bomb.getGridCol(), bomb.getX(), bomb.getY(),
                                            bomb.getExplosionRange());
        calculateExplosionTiles(explosion);
        explosions.add(explosion);
        if (!silentMode) {
            ObjectContainer.getAudioUtil().playEffect(BOOM_BANG);
        }
    }

    private void calculateExplosionTiles(Explosion explosion) {
        int centerRow = explosion.getGridRow();
        int centerCol = explosion.getGridCol();
        int range = explosion.getRange();
        explosion.getExplosionTiles()
                 .add(new Explosion.ExplosionTile(centerRow, centerCol, explosion.getX(), explosion.getY(),
                                                  Explosion.Direction.CENTER, false));
        calculateExplosionDirection(explosion, centerRow, centerCol, -1, 0, range, Explosion.Direction.UP);
        calculateExplosionDirection(explosion, centerRow, centerCol, 1, 0, range, Explosion.Direction.DOWN);
        calculateExplosionDirection(explosion, centerRow, centerCol, 0, -1, range, Explosion.Direction.LEFT);
        calculateExplosionDirection(explosion, centerRow, centerCol, 0, 1, range, Explosion.Direction.RIGHT);

        log.debug("Explosion calculated {} affected tiles", explosion.getExplosionTiles().size());
    }

    private void calculateExplosionDirection(Explosion explosion, int startRow, int startCol, int rowDelta,
                                             int colDelta, int range, Explosion.Direction direction) {
        int xOffset = tileMap.getXDrawOffSet();
        int yOffset = tileMap.getYDrawOffSet();
        for (int i = 1; i <= range; i++) {
            int currentRow = startRow + (i * rowDelta);
            int currentCol = startCol + (i * colDelta);

            if (currentRow < 0 || currentRow >= tileMap.getData().length || currentCol < 0 || currentCol >= tileMap.getData()[0].length) {
                log.trace("Explosion {} stopped: out of bounds at [row={}, col={}]", direction, currentRow, currentCol);
                break;
            }
            if (CollisionUtil.blocksExplosion(currentRow, currentCol, tileMap)) {
                if (CollisionUtil.isDestructible(currentRow, currentCol, tileMap)) {
                    boolean isEnd = true;
                    float tileX = currentCol * io.nghlong3004.constant.GameConstant.TILES_SIZE + xOffset;
                    float tileY = currentRow * io.nghlong3004.constant.GameConstant.TILES_SIZE + yOffset;

                    explosion.getExplosionTiles()
                             .add(new Explosion.ExplosionTile(currentRow, currentCol, tileX, tileY, direction, isEnd));


                    boolean wasGiftBox = io.nghlong3004.util.CollisionUtil.destroyTile(currentRow, currentCol, tileMap);

                    if (wasGiftBox) {
                        trySpawnItem(currentRow, currentCol, tileX, tileY);
                    }

                    log.trace("Explosion {} stopped: destroyed tile at [row={}, col={}]", direction, currentRow,
                              currentCol);
                }
                else {
                    log.trace("Explosion {} stopped: blocked by solid tile at [row={}, col={}]", direction, currentRow,
                              currentCol);
                }
                break;
            }

            boolean isEnd = (i == range);
            float tileX = currentCol * io.nghlong3004.constant.GameConstant.TILES_SIZE + xOffset;
            float tileY = currentRow * io.nghlong3004.constant.GameConstant.TILES_SIZE + yOffset;

            explosion.getExplosionTiles()
                     .add(new Explosion.ExplosionTile(currentRow, currentCol, tileX, tileY, direction, isEnd));
        }
    }

    private void placeBomb(Bomber bomber) {
        float bomberCenterX = bomber.getBox().x + bomber.getBox().width / 2;
        float bomberCenterY = bomber.getBox().y + bomber.getBox().height / 2;

        int xOffset = tileMap.getXDrawOffSet();
        int yOffset = tileMap.getYDrawOffSet();

        float relativeX = bomberCenterX - xOffset;
        float relativeY = bomberCenterY - yOffset;
        int bomberGridCol = (int) (relativeX / (float) io.nghlong3004.constant.GameConstant.TILES_SIZE);
        int bomberGridRow = (int) (relativeY / (float) io.nghlong3004.constant.GameConstant.TILES_SIZE);

        for (Bomb existingBomb : bombs) {
            if (existingBomb.isSameGridPosition(bomberGridRow, bomberGridCol)) {
                log.debug("Cannot place bomb: position [row={}, col={}] already has a bomb", bomberGridRow,
                          bomberGridCol);
                return;
            }
        }

        Bomb bomb = new Bomb(bomberCenterX, bomberCenterY, xOffset, yOffset);
        bomb.setExplosionRange(bomber.getExplosionRange());
        bombs.add(bomb);
        bomber.incrementBombCount();

        if (!silentMode) {
            ObjectContainer.getAudioUtil().playEffect(SET_BOOM);
        }
    }

    public void add(Entity bomber) {
        if (bomber instanceof Bomber) {
            bombers.add((Bomber) bomber);
        }
    }

    public void removeAll() {
        bomberAISystem.shutdown();
        bombers.clear();
        bombs.clear();
        explosions.clear();
        items.clear();
    }

    public void resetAll() {
        bombs.clear();
        bombers.clear();
        explosions.clear();
        items.clear();
    }

    public void cleanup() {
        bomberAISystem.shutdown();
        log.info("GameSystem cleaned up");
    }

    public List<SpawnPoint> getSpawnPoints() {
        return tileMap.getSpawnPoints();
    }

    public boolean areAllEnemiesDead() {
        List<Bomber> bombersCopy = new ArrayList<>(bombers);
        for (Bomber bomber : bombersCopy) {
            if (!bomber.isPlayer() && bomber.isAlive() && !bomber.isDead()) {
                return false;
            }
        }
        return true;
    }

    private void checkBombExitStatus() {
        for (Bomb bomb : bombs) {
            if (!bomb.isAllowEntityExit()) {
                continue;
            }
            boolean entityStillOnBomb = false;
            List<Bomber> bombersCopyForBombExit = new ArrayList<>(bombers);
            for (Bomber bomber : bombersCopyForBombExit) {
                if (bomber.getBox().intersects(bomb.getBox())) {
                    entityStillOnBomb = true;
                    break;
                }
            }

            if (!entityStillOnBomb) {
                bomb.setAllowEntityExit(false);
                log.trace("Bomb at [{},{}] locked - entity exited", bomb.getGridRow(), bomb.getGridCol());
            }
        }
    }

    private void checkExplosionDamage(Explosion explosion) {

        List<Bomber> bombersCopyForExplosion = new ArrayList<>(bombers);
        for (var bomber : bombersCopyForExplosion) {
            if (!bomber.isAlive()) {
                continue;
            }

            for (var explosionTile : explosion.getExplosionTiles()) {
                if (isEntityInExplosionTile(bomber, explosionTile)) {
                    bomber.die();
                    String entityType = bomber.isPlayer() ? "Player" : "Enemy";
                    log.info("{} killed by explosion at grid [row={}, col={}]", entityType, explosionTile.getGridRow(),
                             explosionTile.getGridCol());
                    break;
                }
            }
        }
    }

    private boolean isEntityInExplosionTile(Entity entity, Explosion.ExplosionTile explosionTile) {
        float entityCenterX = entity.getBox().x + entity.getBox().width / 2f;
        float entityCenterY = entity.getBox().y + entity.getBox().height / 2f;

        int xOffset = tileMap.getXDrawOffSet();
        int yOffset = tileMap.getYDrawOffSet();

        float relativeX = entityCenterX - xOffset;
        float relativeY = entityCenterY - yOffset;

        int entityGridCol = (int) (relativeX / io.nghlong3004.constant.GameConstant.TILES_SIZE);
        int entityGridRow = (int) (relativeY / io.nghlong3004.constant.GameConstant.TILES_SIZE);

        return entityGridRow == explosionTile.getGridRow() && entityGridCol == explosionTile.getGridCol();
    }


    private void checkWalkOverDeadBomber(Bomber aliveBomber, List<Bomber> bombersList,
                                         List<Bomber> deadBombersToRemove) {
        if (!aliveBomber.isAlive() || aliveBomber.isDead()) {
            return;
        }

        for (Bomber deadBomber : bombersList) {
            if (!deadBomber.isDead()) {
                continue;
            }

            if (aliveBomber.getBox().intersects(deadBomber.getBox())) {
                deadBombersToRemove.add(deadBomber);
            }
        }
    }

    private void trySpawnItem(int gridRow, int gridCol, float tileX, float tileY) {

        if (Math.random() > io.nghlong3004.constant.ItemConstant.ITEM_DROP_CHANCE) {
            return;
        }

        ItemType itemType;
        double random = Math.random();

        if (random < io.nghlong3004.constant.ItemConstant.BOMB_DROP_CHANCE) {
            itemType = ItemType.BOMB;
        }
        else if (random < io.nghlong3004.constant.ItemConstant.BOMB_DROP_CHANCE + io.nghlong3004.constant.ItemConstant.BOMB_SIZE_DROP_CHANCE) {
            itemType = ItemType.BOMB_SIZE;
        }
        else {
            itemType = ItemType.SHOE;
        }

        int itemWidth = io.nghlong3004.constant.ItemConstant.ITEM_WIDTH;
        int itemHeight = io.nghlong3004.constant.ItemConstant.ITEM_HEIGHT;
        float centerOffsetX = (io.nghlong3004.constant.GameConstant.TILES_SIZE - itemWidth) / 2f;
        float centerOffsetY = (io.nghlong3004.constant.GameConstant.TILES_SIZE - itemHeight) / 2f;

        float itemX = tileX + centerOffsetX;
        float itemY = tileY + centerOffsetY;

        Item item = new Item(itemType, itemX, itemY, itemWidth, itemHeight);
        items.add(item);

        log.info("Spawned {} at grid [row={}, col={}], position [x={}, y={}]", itemType.displayName, gridRow, gridCol,
                 itemX, itemY);
    }

    private void checkItemCollection(Item item) {
        if (item.isCollected()) {
            return;
        }

        List<Bomber> bombersCopy = new ArrayList<>(bombers);
        for (Bomber bomber : bombersCopy) {
            if (!bomber.isAlive() || bomber.isDead()) {
                continue;
            }


            if (bomber.getBox().intersects(item.getBox())) {
                applyItemEffect(bomber, item);
                item.collect();


                if (!silentMode) {
                    ObjectContainer.getAudioUtil().playEffect(io.nghlong3004.constant.AudioConstant.ITEM);
                }

                log.info("{} collected {} item", bomber.isPlayer() ? "Player" : "AI", item.getType().displayName);
                break;
            }
        }
    }

    private void applyItemEffect(Bomber bomber, Item item) {
        switch (item.getType()) {
            case BOMB -> {
                bomber.setMaxBombs(bomber.getMaxBombs() + io.nghlong3004.constant.ItemConstant.BOMB_INCREMENT);
                log.debug("Bomber max bombs increased to {}", bomber.getMaxBombs());
            }
            case BOMB_SIZE -> {
                bomber.setExplosionRange(
                        bomber.getExplosionRange() + io.nghlong3004.constant.ItemConstant.BOMB_SIZE_INCREMENT);
                log.debug("Bomber explosion range increased to {}", bomber.getExplosionRange());
            }
            case SHOE -> {
                float currentBoost = bomber.getSpeedBoost();
                float newBoost = Math.min(currentBoost + io.nghlong3004.constant.ItemConstant.SPEED_INCREMENT,
                                          io.nghlong3004.constant.ItemConstant.MAX_SPEED_BOOST);
                bomber.setSpeedBoost(newBoost);
                log.debug("Bomber speed boost increased to {}", newBoost);
            }
        }
    }
}
