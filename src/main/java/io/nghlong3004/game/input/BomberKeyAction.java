package io.nghlong3004.game.input;

import io.nghlong3004.model.entities.Bomber;
import lombok.AllArgsConstructor;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@AllArgsConstructor
public enum BomberKeyAction {
    MOVE_LEFT(new int[]{KeyEvent.VK_A, KeyEvent.VK_LEFT}, (bomber) -> bomber.setLeft(true),
              (bomber) -> bomber.setLeft(false)),

    MOVE_RIGHT(new int[]{KeyEvent.VK_D, KeyEvent.VK_RIGHT}, (bomber) -> bomber.setRight(true),
               (bomber) -> bomber.setRight(false)),

    MOVE_UP(new int[]{KeyEvent.VK_W, KeyEvent.VK_UP}, (bomber) -> bomber.setUp(true), (bomber) -> bomber.setUp(false)),

    MOVE_DOWN(new int[]{KeyEvent.VK_S, KeyEvent.VK_DOWN}, (bomber) -> bomber.setDown(true),
              (bomber) -> bomber.setDown(false)),

    PLACE_BOMB(new int[]{KeyEvent.VK_SPACE, KeyEvent.VK_NUMPAD0}, (bomber) -> bomber.setPlaceBombRequested(true),
               (bomber) -> bomber.setPlaceBombRequested(false));


    public final int[] keys;
    public final Consumer<Bomber> onPressed;
    public final Consumer<Bomber> onReleased;

    public static final Map<Integer, BomberKeyAction> LOOKUP = new HashMap<>();

    static {
        initLookup();
    }

    public static int getIndexFromKeyCode(int[] keys, int keyCode) {
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i] == keyCode) {
                return i;
            }
        }
        return -1;
    }

    private static void initLookup() {
        for (var act : values()) {
            for (int k : act.keys) {
                LOOKUP.put(k, act);
            }
        }
    }
}
