package io.nghlong3004.game.component.online;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OnlineState;
import io.nghlong3004.game.manager.GameManager;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OnlineType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
public class OnlinePlayComponent extends GameComponent {

    private final NetworkManager networkManager;
    private final GameManager gameManager;
    @Setter
    private List<Bomber> bombers;
    @Setter
    private boolean isPlaying;

    public OnlinePlayComponent(GameContext context) {
        super(context);
        this.networkManager = context.getNetworkManager();
        this.gameManager = context.getGameManager();
    }

    public void play() {
        this.bombers = networkManager.getBombers();
        this.isPlaying = true;
        networkManager.setPlaying(false);
        this.gameManager.playOnline(this.networkManager);
    }

    public void exit() {
        this.isPlaying = false;
        gameManager.reset();
    }

    @Override
    public void update() {
        if (!isPlaying) {
            return;
        }
        networkManager.update();
        gameManager.update();
        if (gameManager.getBomberManager()
                       .getBombers()
                       .isEmpty()) {
            return;
        }

        if (!gameManager.isBomberLocalAlive(networkManager.getBomberId())) {
            this.isPlaying = false;
            var playingState = (OnlineState) context.getGameState(GameStateType.ONLINE);
            playingState.setType(OnlineType.OVER);
            log.info("Player lose!");
            return;
        }
        if (!gameManager.isBomberNotLocalAlive(networkManager.getBomberId())) {
            this.isPlaying = false;
            var playingState = (OnlineState) context.getGameState(GameStateType.ONLINE);
            playingState.setType(OnlineType.WIN);
            log.info("Player win!");
        }
    }

    @Override
    public void render(Graphics g) {
        if (!isPlaying) {
            return;
        }
        gameManager.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        networkManager.keyAction(e.getKeyCode(), networkManager.getBomberId(), false);
        networkManager.sendGameAction(e.getKeyCode(), false);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        networkManager.keyAction(e.getKeyCode(), networkManager.getBomberId(), true);
        networkManager.sendGameAction(e.getKeyCode(), true);
    }

}
