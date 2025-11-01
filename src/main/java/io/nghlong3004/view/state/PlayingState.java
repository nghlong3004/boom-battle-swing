package io.nghlong3004.view.state;

import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.constant.MapConstant;
import io.nghlong3004.model.*;
import io.nghlong3004.system.GameStateContextHolder;
import io.nghlong3004.system.GameSystem;
import io.nghlong3004.util.ObjectContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import static io.nghlong3004.constant.BomberConstant.BOMBER_HEIGHT;
import static io.nghlong3004.constant.BomberConstant.BOMBER_WIDTH;

@Slf4j
public class PlayingState implements GameState {

    @Getter
    private Entity bomber;
    private final GameSystem gameSystem;
    private final StateContext stateContext;
    private final Random random;
    private int deathCheckDelay = 0;

    protected PlayingState(StateContext stateContext) {
        this.stateContext = stateContext;
        this.random = new Random();
        this.gameSystem = new GameSystem();

    }

    private void loadMapAndSpawnEntities() {
        String mapPath = MapConstant.FILE_PATH_TEMPLATE.formatted(GameStateContextHolder.MODE.name);
        gameSystem.getTileMap().setMode(GameStateContextHolder.MODE);
        gameSystem.getTileMap().importDataMap(mapPath);

        SpawnPoint playerSpawn = gameSystem.getTileMap().getPlayerSpawn();
        if (playerSpawn != null) {
            float centerX = playerSpawn.getCenteredX((int) BOMBER_WIDTH);
            float centerY = playerSpawn.getCenteredY((int) BOMBER_HEIGHT);
            this.bomber = new Bomber(centerX, centerY, (int) BOMBER_WIDTH, (int) BOMBER_HEIGHT, true);
            this.gameSystem.add(bomber);
            log.info("Player spawned at grid [{},{}], centered pos [{},{}]", playerSpawn.getGridRow(),
                     playerSpawn.getGridCol(), centerX, centerY);
        }
        else {
            this.bomber = new Bomber(264, 200f, (int) BOMBER_WIDTH, (int) BOMBER_HEIGHT, true);
            this.gameSystem.add(bomber);
            log.warn("No player spawn point found, using default position");
        }

        var enemySpawns = gameSystem.getTileMap().getSoldierSpawns();
        int enemyCount = 0;
        for (SpawnPoint spawn : enemySpawns) {
            int skinIndex = random.nextInt(3) + 1;

            float centerX = spawn.getCenteredX((int) BOMBER_WIDTH);
            float centerY = spawn.getCenteredY((int) BOMBER_HEIGHT);

            log.info("Spawning enemy {} at grid [{},{}], centered pos [{},{}], skin={}", enemyCount, spawn.getGridRow(),
                     spawn.getGridCol(), centerX, centerY, skinIndex);

            Bomber enemy = new Bomber(centerX, centerY, (int) BOMBER_WIDTH, (int) BOMBER_HEIGHT, false, skinIndex);
            gameSystem.add(enemy);

            enemyCount++;
        }
    }

    public void reset() {
        log.info("Resetting PlayingState - clearing all entities and reloading map");
        gameSystem.resetAll();
        deathCheckDelay = 0;
    }

    @Override
    public void update() {
        gameSystem.update();


        if (GameStateContextHolder.GAME_MODE == GameMode.OFFLINE && bomber instanceof Bomber playerBomber) {
            if (playerBomber.isDead()) {
                deathCheckDelay++;

                if (deathCheckDelay >= 120) {
                    log.info("Player died in offline mode, showing Game Over screen");
                    stateContext.changeState(State.GAME_OVER);
                    deathCheckDelay = 0;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        gameSystem.render(g);
    }

    @Override
    public void exit() {
        ObjectContainer.getAudioUtil().stopSong();
    }

    @Override
    public void enter() {
        loadMapAndSpawnEntities();
        ObjectContainer.getAudioUtil().playSong(GameStateContextHolder.MODE.index);
        ObjectContainer.getAudioUtil().playEffect(AudioConstant.START);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> bomber.setLeft(true);
            case KeyEvent.VK_D -> bomber.setRight(true);
            case KeyEvent.VK_W -> bomber.setUp(true);
            case KeyEvent.VK_S -> bomber.setDown(true);
            case KeyEvent.VK_SPACE -> {
                if (bomber instanceof Bomber) {
                    ((Bomber) bomber).requestPlaceBomb();
                }
            }
            case KeyEvent.VK_ENTER -> {
                stateContext.changeState(State.PAUSED);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> bomber.setLeft(false);
            case KeyEvent.VK_D -> bomber.setRight(false);
            case KeyEvent.VK_W -> bomber.setUp(false);
            case KeyEvent.VK_S -> bomber.setDown(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

}
