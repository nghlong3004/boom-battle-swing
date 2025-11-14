package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.PlayingState;
import io.nghlong3004.game.input.BomberKeyAction;
import io.nghlong3004.game.manager.GameManager;
import io.nghlong3004.game.manager.ManagerFactory;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.PlayType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

@Slf4j
public class GamePlayComponent extends PlayComponent {
    @Getter
    private final GameManager gameManager;
    private final Bomber[] bombers;
    private final Map<Integer, BomberKeyAction> lookup;

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
        gameManager.play(context.getMapType(), getPlayers());
    }

    private List<Bomber> getPlayers() {
        return new ArrayList<>(Arrays.asList(this.bombers)
                                     .subList(0, context.getNumberBomber()));
    }

    private void loadBombers() {
        for (int i = 0; i < context.getNumberBomber(); ++i) {
            bombers[i] = new Bomber(0, 0, context.getSkinType()[i]);
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
            PlayingState playingState = (PlayingState) context.getGameState(GameStateType.OFFLINE);
            gameManager.getAgentManager()
                       .stop();
            playingState.setType(PlayType.OVER);
            log.info("Player lose!");
            return;
        }

        if (!gameManager.isAnyAgentAlive()) {
            PlayingState playingState = (PlayingState) context.getGameState(GameStateType.OFFLINE);
            gameManager.getAgentManager()
                       .stop();
            playingState.setType(PlayType.WIN);
            log.info("All agents defeated! Player wins!");
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
