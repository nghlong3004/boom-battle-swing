package io.nghlong3004.boom_battle_swing.view;

import io.nghlong3004.boom_battle_swing.constant.AudioConstant;
import io.nghlong3004.boom_battle_swing.model.GameState;
import io.nghlong3004.boom_battle_swing.util.ObjectContainer;
import io.nghlong3004.boom_battle_swing.view.game.GameAudioStateObserver;
import io.nghlong3004.boom_battle_swing.view.game.GameLoop;
import io.nghlong3004.boom_battle_swing.view.game.GamePanel;
import io.nghlong3004.boom_battle_swing.view.game.GameWindow;
import io.nghlong3004.boom_battle_swing.view.scene.SceneRouter;
import io.nghlong3004.boom_battle_swing.view.scene.component.MenuComponent;
import io.nghlong3004.boom_battle_swing.view.scene.component.OptionComponent;
import io.nghlong3004.boom_battle_swing.view.scene.component.PlayingComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
@Getter
public class GameApplication {
    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private final SceneRouter router;
    private final MenuComponent menu;
    private final PlayingComponent playing;
    private final OptionComponent option;
    private Thread loopThread;
    private GameLoop loop;

    public GameApplication() {
        this.menu = new MenuComponent(this);
        this.playing = new PlayingComponent(this);
        this.option = new OptionComponent(this);

        this.router = new SceneRouter(menu, playing, option);
        this.router.setOnStateChanged(new GameAudioStateObserver());

        this.gamePanel = new GamePanel(this);
        this.gameWindow = new GameWindow(gamePanel);
        this.gamePanel.requestFocus();

        ObjectContainer.getAudioPlayer().playSong(AudioConstant.MENU);

        startLoop();
    }

    private void startLoop() {
        loop = new GameLoop(() -> {
            router.update();
            if (GameState.state == GameState.QUIT) {
                shutdown();
            }
        }, gamePanel::repaint);
        loopThread = new Thread(loop, "game-loop");
        loopThread.start();
    }

    public void shutdown() {
        try {
            if (loop != null) {
                loop.stop();
            }
            ObjectContainer.getAudioPlayer().playSong(AudioConstant.BYE_BYE);
            System.exit(0);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public void render(Graphics g) {
        router.render(g);
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            router.getPlaying().getBomber().resetDirection();
        }
    }
}
