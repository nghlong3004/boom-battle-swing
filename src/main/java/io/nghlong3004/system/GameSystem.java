package io.nghlong3004.system;

import io.nghlong3004.model.*;
import io.nghlong3004.util.CollisionUtil;
import io.nghlong3004.util.ObjectContainer;
import lombok.Getter;
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
    private final RenderSystem tileMapRenderer, entityRenderer, bombRenderer, explosionRenderer;
    private final List<Bomber> bombers;
    private final List<Bomb> bombs;
    private final List<Explosion> explosions;
    @Getter
    private final TileMap tileMap;
    private long lastMoveSfxAtMs = 0L;

    public GameSystem() {
        this.tileMapRenderer = new TileMapRenderSystem();
        this.tileMapUpdater = new TileMapUpdateSystem();
        this.entityRenderer = new EntityRenderSystem();
        this.entityUpdater = new EntityUpdateSystem();
        this.bombRenderer = new BombRenderSystem();
        this.bombUpdater = new BombUpdateSystem();
        this.explosionRenderer = new ExplosionRenderSystem();
        this.bomberAISystem = new BomberAISystem();
        this.bombers = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.tileMap = new TileMap();

        this.entityUpdater.setTileMap(tileMap);
        this.entityUpdater.setBombs(bombs);
        log.info("GameSystem initialized successfully");
    }

    public void update() {
        tileMapUpdater.update(tileMap);

        List<Entity> allEntities = new ArrayList<>(bombers);
        entityUpdater.setAllEntities(allEntities);

        bomberAISystem.setAllBombers(bombers);
        bomberAISystem.setBombs(bombs);
        bomberAISystem.setExplosions(explosions);
        bomberAISystem.setTileMap(tileMap);


        List<Bomber> deadBombersToRemove = new ArrayList<>();

        for (var bomber : bombers) {

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


            checkWalkOverDeadBomber(bomber, deadBombersToRemove);
        }


        bombers.removeAll(deadBombersToRemove);

        checkBombExitStatus();

        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            bombUpdater.update(bomb);

            if (bomb.isExploded()) {
                createExplosion(bomb);

                for (var bomber : bombers) {
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

        List<Bomber> bombersCopy = new ArrayList<>(bombers);
        for (var bomber : bombersCopy) {
            entityRenderer.render(g, bomber);
            if (bomber.isPlayer() && bomber.isMoving()) {
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

        ObjectContainer.getAudioUtil().playEffect(BOOM_BANG);
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

                    io.nghlong3004.util.CollisionUtil.destroyTile(currentRow, currentCol, tileMap);
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
        bombs.add(bomb);
        bomber.incrementBombCount();

        ObjectContainer.getAudioUtil().playEffect(SET_BOOM);
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
    }

    public void resetAll() {
        bombs.clear();
        bombers.clear();
        explosions.clear();
    }


    public void cleanup() {
        bomberAISystem.shutdown();
        log.info("GameSystem cleaned up");
    }

    public List<SpawnPoint> getSpawnPoints() {
        return tileMap.getSpawnPoints();
    }

    private void checkBombExitStatus() {
        for (Bomb bomb : bombs) {
            if (!bomb.isAllowEntityExit()) {
                continue;
            }

            boolean entityStillOnBomb = false;

            for (Bomber bomber : bombers) {
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
        for (var bomber : bombers) {
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


    private void checkWalkOverDeadBomber(Bomber aliveBomber, List<Bomber> deadBombersToRemove) {
        if (!aliveBomber.isAlive() || aliveBomber.isDead()) {
            return;
        }


        for (Bomber deadBomber : bombers) {
            if (!deadBomber.isDead()) {
                continue;
            }


            if (aliveBomber.getBox().intersects(deadBomber.getBox())) {
                deadBombersToRemove.add(deadBomber);
            }
        }
    }
}
