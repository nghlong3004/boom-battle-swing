package io.nghlong3004.component.play;

import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.PlayingState;
import io.nghlong3004.entity.Bomber;
import io.nghlong3004.input.BomberKeyAction;
import io.nghlong3004.manager.GameManager;
import io.nghlong3004.manager.ManagerFactory;
import io.nghlong3004.type.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

@Slf4j
public class GamePlayComponent extends PlayComponent {

    private final GameManager gameManager;
    private Bomber[] bombers;
    private final Map<Integer, BomberKeyAction> lookup;
    private static final int MAX_CHARACTERS = 4;

    public GamePlayComponent(GameContext context) {
        super(context);
        this.gameManager = ManagerFactory.createGameManager();
        this.lookup = new HashMap<>();
        initLookup();
        bombers = new Bomber[2];
    }

    public void play() {
        gameManager.reset();
        loadBombers();
        if (context.getGameType() == GameType.OFFLINE) {
            loadEnemies();
        }
        gameManager.play(context.getMapType(), getPlayers());
    }

    private List<Bomber> getPlayers() {
        var players = new ArrayList<Bomber>();
        players.addAll(Arrays.asList(this.bombers)
                             .subList(0, context.getNumberBomber()));
        return players;
    }

    private void loadBombers() {
        for (int i = 0; i < context.getNumberBomber(); ++i) {
            bombers[i] = new Bomber(0, 0, context.getSkinType()[i]);
        }
    }

    private void loadEnemies() {
        int playerCount = context.getNumberBomber();
        int enemyCount = MAX_CHARACTERS - playerCount;

        log.info("Loading {} enemies for {} player(s)", enemyCount, playerCount);

        SkinType[] enemySkins = {SkinType.BOZ, SkinType.EVIE, SkinType.PLUNK};
        EnemyAIType[] aiTypes = {EnemyAIType.EASY, EnemyAIType.NORMAL, EnemyAIType.HARD};

        for (int i = 0; i < enemyCount; i++) {
            SkinType skin = enemySkins[i % enemySkins.length];
            EnemyAIType aiType = aiTypes[i % aiTypes.length];

            log.info("Created enemy {} with AI type: {}", i + 1, aiType);
        }
    }

    public void exit() {
        gameManager.reset();
    }


    @Override
    public void update() {
        gameManager.update();

        if (gameManager.getBomberManager()
                       .getBombers()
                       .isEmpty()) {
            return;
        }

        if (!gameManager.isAnyPlayerAlive()) {
            PlayingState playingState = (PlayingState) context.getGameState(GameStateType.PLAYING);
            playingState.setType(PlayType.OVER);
        }
    }

    @Override
    public void render(Graphics g) {
        gameManager.render(g);
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
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        var action = lookup.get(e.getKeyCode());
        int index = getIndexFromKeyEvent(action, e);
        if (index != -1) {
            action.onPress.accept(bombers[index]);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var action = lookup.get(e.getKeyCode());
        int index = getIndexFromKeyEvent(action, e);
        if (index != -1) {
            action.onRelease.accept(bombers[index]);
        }
    }

    private int getIndexFromKeyEvent(BomberKeyAction action, KeyEvent e) {
        if (action != null) {
            int id = getIndexFromKeyCode(action.keys, e.getKeyCode());
            if (id != -1 && bombers[id] != null) {
                return id;
            }
        }
        return -1;
    }

    private int getIndexFromKeyCode(int[] keys, int keyCode) {
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i] == keyCode) {
                return i;
            }
        }
        return -1;
    }

    private void initLookup() {
        for (var act : BomberKeyAction.values()) {
            for (int k : act.keys) {
                lookup.put(k, act);
            }
        }
    }
}
