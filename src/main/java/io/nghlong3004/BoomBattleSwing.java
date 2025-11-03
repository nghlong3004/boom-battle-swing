package io.nghlong3004;

import io.nghlong3004.game.GameFactory;
import io.nghlong3004.game.GameWindow;

public class BoomBattleSwing {
    public static void main(String[] args) {
        GameWindow window = GameFactory.createBoomBattle();
        window.open();
    }
}
