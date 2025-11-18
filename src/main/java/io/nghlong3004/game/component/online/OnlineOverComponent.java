package io.nghlong3004.game.component.online;

import io.nghlong3004.game.component.OverComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OnlineState;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OnlineType;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.MouseEvent;

@Slf4j
public class OnlineOverComponent extends OverComponent {
    private final NetworkManager networkManager;

    public OnlineOverComponent(GameContext context) {
        super(context);
        networkManager = context.getNetworkManager();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                var onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
                if (onlineState != null) {
                    ((OnlinePlayComponent) onlineState.getComponent(OnlineType.PLAYING)).exit();
                    networkManager.leaveRoom();
                    onlineState.setType(OnlineType.ROOM_LIST);
                }
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                var onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
                if (onlineState != null) {
                    ((OnlinePlayComponent) onlineState.getComponent(OnlineType.PLAYING)).exit();
                    onlineState.setType(OnlineType.ROOM);
                }
                resetAnimation();
                log.info("Game reset from Game Over, change to room");
            }
        }

        super.mouseReleased(e);
    }
}
