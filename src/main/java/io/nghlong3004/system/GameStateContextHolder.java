package io.nghlong3004.system;

import io.nghlong3004.model.BomberSkin;
import io.nghlong3004.model.GameMode;
import io.nghlong3004.model.State;
import io.nghlong3004.model.TileMode;

public class GameStateContextHolder {

    public static State STATE = State.MENU;

    public static BomberSkin SKIN = BomberSkin.BOZ;

    public static TileMode MODE = TileMode.DESERT_MODE;
    
    public static GameMode GAME_MODE = GameMode.OFFLINE;  // Default to offline
}
