package io.nghlong3004.boom_battle_swing;

import io.nghlong3004.boom_battle_swing.view.GameApplication;

import javax.swing.*;

public class BoomBattleSwingApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameApplication::new);
    }
}
