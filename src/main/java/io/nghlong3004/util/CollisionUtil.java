package io.nghlong3004.util;

import static io.nghlong3004.constant.GameConstant.*;

public final class CollisionUtil {

    private CollisionUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static int calculateMapOffsetX(int mapColumns) {
        return (MAX_SCREEN_COLUMN - mapColumns) >>> 1;
    }

    public static int calculateMapOffsetY(int mapRows) {
        return (MAX_SCREEN_ROW - mapRows) >>> 1;
    }

    public static int pixelToGridRow(float pixelY, int offsetY) {
        return (int) (pixelY / TILE_SIZE) - offsetY;
    }

    public static int pixelToGridColumn(float pixelX, int offsetX) {
        return (int) (pixelX / TILE_SIZE) - offsetX;
    }
}
