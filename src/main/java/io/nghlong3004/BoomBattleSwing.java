package io.nghlong3004;

import io.nghlong3004.game.Game;

import javax.swing.*;

public class BoomBattleSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::run);
    }
}
