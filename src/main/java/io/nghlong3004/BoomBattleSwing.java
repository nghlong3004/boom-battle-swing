package io.nghlong3004;

import io.nghlong3004.game.GameFactory;
import io.nghlong3004.game.GameWindow;

import javax.swing.*;

public class BoomBattleSwing {
    public static void main(String[] args) {
        GameWindow window = GameFactory.createBoomBattle();
        SwingUtilities.invokeLater(window::open);
    }
}
