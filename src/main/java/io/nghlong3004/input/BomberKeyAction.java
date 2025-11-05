package io.nghlong3004.input;

import io.nghlong3004.entity.Bomber;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

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
    public final Consumer<Bomber> onPress;
    public final Consumer<Bomber> onRelease;

    BomberKeyAction(int[] keys, Consumer<Bomber> onPress, Consumer<Bomber> onRelease) {
        this.keys = keys;
        this.onPress = onPress;
        this.onRelease = onRelease;
    }
}
