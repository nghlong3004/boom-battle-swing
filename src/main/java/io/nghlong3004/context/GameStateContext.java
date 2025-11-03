package io.nghlong3004.context;

import io.nghlong3004.context.state.GameStateID;
import io.nghlong3004.entity.GameType;
import io.nghlong3004.entity.MapType;
import io.nghlong3004.entity.Skin;

public class GameStateContext {
    public static GameStateID STATE;
    public static Skin SKIN = Skin.BOZ;

    public static MapType MAP_TYPE = MapType.DESERT_MODE;

    public static GameType GAME_TYPE = GameType.OFFLINE;
}
