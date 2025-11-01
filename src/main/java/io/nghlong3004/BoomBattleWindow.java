package io.nghlong3004;

import io.nghlong3004.view.BoomBattle;
import io.nghlong3004.view.GameFactory;

public class BoomBattleWindow {

    public static void main(String[] args) {
        BoomBattle window = GameFactory.createBoomBattle();
        window.start();
    }

}
