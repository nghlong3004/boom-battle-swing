package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.component.WinComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OfflineState;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OfflineType;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.MouseEvent;

@Slf4j
public class OfflineWinComponent extends WinComponent {

    public OfflineWinComponent(GameContext context) {
        super(context);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                context.changeState(GameStateType.MENU);
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                ((OfflineState) context.getGameState(GameStateType.OFFLINE)).setType(OfflineType.PLAYING);

            }
        }
        super.mouseReleased(e);
    }
}
