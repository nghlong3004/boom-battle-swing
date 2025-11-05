package io.nghlong3004.component.play;

import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.PlayingState;
import io.nghlong3004.entity.Bomber;
import io.nghlong3004.input.BomberKeyAction;
import io.nghlong3004.manager.GameManager;
import io.nghlong3004.manager.ManagerFactory;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.GameType;
import io.nghlong3004.type.PlayType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GamePlayComponent extends PlayComponent {

    private final GameManager gameManager;
    private Bomber[] bombers;
    private final List<Bomber> enemyList;
    private final Map<Integer, BomberKeyAction> lookup;

    public GamePlayComponent(GameContext context) {
        super(context);
        this.gameManager = ManagerFactory.createGameManager();
        this.lookup = new HashMap<>();
        initLookup();
        bombers = new Bomber[2];
        enemyList = new ArrayList<>();
    }

    public void play() {
        gameManager.reset();
        loadBombers();
        if (context.getGameType() == GameType.OFFLINE) {
            loadEnemy();
        }
        gameManager.play(context.getMapType(), getAllBombers());
    }

    private List<Bomber> getAllBombers() {
        var bombers = new ArrayList<Bomber>();
        bombers.addAll(Arrays.asList(this.bombers)
                             .subList(0, context.getNumberBomber()));
        bombers.addAll(this.enemyList);
        return bombers;
    }

    private void loadBombers() {
        for (int i = 0; i < context.getNumberBomber(); ++i) {
            bombers[i] = new Bomber(0, 0, context.getSkinType()[i]);
        }
    }

    private void loadEnemy() {

    }

    public void exit() {
        enemyList.clear();
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
