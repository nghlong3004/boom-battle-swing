package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.component.OverComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OfflineState;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OfflineType;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.MouseEvent;

@Slf4j
public class OfflineOverComponent extends OverComponent {
    public OfflineOverComponent(GameContext context) {
        super(context);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                OfflineState playingState = (OfflineState) context.getGameState(GameStateType.OFFLINE);
                if (playingState != null) {
                    ((OfflinePlayComponent) playingState.getComponent(OfflineType.PLAYING)).exit();
                }
                context.changeState(GameStateType.MENU);
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                OfflineState playingState = (OfflineState) context.getGameState(GameStateType.OFFLINE);
                if (playingState != null) {
                    ((OfflinePlayComponent) playingState.getComponent(OfflineType.PLAYING)).play();
                    playingState.setType(OfflineType.PLAYING);
                }
                resetAnimation();
                log.info("Game reset from Game Over, restarting...");
            }
        }
        super.mouseReleased(e);
    }
}
