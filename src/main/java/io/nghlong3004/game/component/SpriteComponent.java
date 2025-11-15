package io.nghlong3004.game.component;

import io.nghlong3004.game.component.button.GameButton;
import io.nghlong3004.game.component.button.SpriteButton;
import io.nghlong3004.game.component.offline.OfflinePlayComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OfflineState;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OfflineType;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.GAME_HEIGHT;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;

@Slf4j
public class SpriteComponent extends GameComponent {
    private SpriteButton unpauseButton;
    private SpriteButton replayButton;
    private SpriteButton homeButton;

    private final List<GameButton> buttons;

    public SpriteComponent(GameContext context) {
        super(context);
        createSpritesButton();
        buttons = List.of(unpauseButton, replayButton, homeButton);
    }

    private void createSpritesButton() {
        // URM_BUTTON_SIZE + URM_BUTTON_SIZE * 6 / 5 + URM_BUTTON_SIZE * 6 / 5 = 17 / 5 * URM_BUTTON_SIZE
        int homeX = GAME_WIDTH - URM_BUTTON_SIZE * 17 / 5 >>> 1;
        int unPauseX = homeX + URM_BUTTON_SIZE * 6 / 5;
        int relayX = unPauseX + URM_BUTTON_SIZE * 6 / 5;
        int y = GAME_HEIGHT / 2 + URM_BUTTON_SIZE * 2;
        unpauseButton = new SpriteButton(unPauseX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        replayButton = new SpriteButton(relayX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
        homeButton = new SpriteButton(homeX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                OfflineState playingState = (OfflineState) context.getGameState(GameStateType.OFFLINE);
                if (playingState.getComponent(OfflineType.PLAYING) instanceof OfflinePlayComponent) {
                    ((OfflinePlayComponent) playingState.getComponent(OfflineType.PLAYING)).exit();
                }
                playingState.setType(OfflineType.PLAYING);
                context.changeState(GameStateType.MENU);
            }
        }
        else if (unpauseButton.isMouseOver(e)) {
            if (unpauseButton.isMousePressed()) {
                OfflineState playingState = ((OfflineState) context.getGameState(GameStateType.OFFLINE));
                playingState.setType(OfflineType.PLAYING);
                playingState.unpause();
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                OfflineState playingState = (OfflineState) context.getGameState(GameStateType.OFFLINE);
                playingState.replay();
                playingState.setType(OfflineType.PLAYING);
            }
        }

        for (var gameButton : buttons) {
            gameButton.reset();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var gameButton : buttons) {
            gameButton.setMouseOver(false);
        }
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void update() {
        for (var gameObject : buttons) {
            gameObject.update();
        }
    }

    @Override
    public void render(Graphics g) {
        for (var gameObject : buttons) {
            gameObject.render(g);
        }
    }
}
