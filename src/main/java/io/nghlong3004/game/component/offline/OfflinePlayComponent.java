package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OfflineState;
import io.nghlong3004.game.input.BomberKeyAction;
import io.nghlong3004.game.manager.GameManager;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OfflineType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.nghlong3004.game.input.BomberKeyAction.LOOKUP;
import static io.nghlong3004.game.input.BomberKeyAction.getIndexFromKeyCode;

@Slf4j
public class OfflinePlayComponent extends GameComponent {
    @Getter
    private final GameManager gameManager;
    private final Bomber[] bombers;

    public OfflinePlayComponent(GameContext context) {
        super(context);
        this.gameManager = context.getGameManager();
        bombers = new Bomber[2];
    }

    public void play() {
        loadBombers();
        gameManager.playOffline(context.getMapType(), getPlayers());
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
            OfflineState playingState = (OfflineState) context.getGameState(GameStateType.OFFLINE);
            gameManager.getAgentManager()
                       .stop();
            playingState.setType(OfflineType.OVER);
            log.info("Player lose!");
            return;
        }

        if (!gameManager.isAnyAgentAlive()) {
            OfflineState playingState = (OfflineState) context.getGameState(GameStateType.OFFLINE);
            gameManager.getAgentManager()
                       .stop();
            playingState.setType(OfflineType.WIN);
            log.info("All agents defeated! Player wins!");
        }
    }

    @Override
    public void render(Graphics g) {
        gameManager.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        var action = LOOKUP.get(e.getKeyCode());
        int index = getIndexFromKeyEvent(action, e);
        if (index != -1) {
            action.onPressed.accept(bombers[index]);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var action = LOOKUP.get(e.getKeyCode());
        int index = getIndexFromKeyEvent(action, e);
        if (index != -1) {
            action.onReleased.accept(bombers[index]);
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
}
