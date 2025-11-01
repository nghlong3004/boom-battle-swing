package io.nghlong3004.view;

import io.nghlong3004.view.state.StateContext;

public class GameFactory {

    public static BoomBattle createBoomBattle() {
        StateContext context = new StateContext();
        GamePanel gamePanel = new GamePanel(context);
        GameFrame gameFrame = new GameFrame(gamePanel);
        GameLoop gameLoop = new GameLoop(context, gamePanel);
        Thread thread = new Thread(gameLoop, "game-loop");
        gamePanel.requestFocus();
        return BoomBattle.builder().context(context).gamePanel(gamePanel).gameFrame(gameFrame).thread(thread).build();
    }

}
