package io.nghlong3004.util;

import io.nghlong3004.loader.AudioLoader;
import lombok.Setter;

import static io.nghlong3004.constant.AudioConstant.*;

public class AudioHelper {
    @Setter
    private static AudioLoader audioLoader;

    public static void playTouchSound() {
        
    }

    public static void playClickSound() {
        if (audioLoader != null) {
            audioLoader.playEffect(CLICK);
        }
    }

    public static void playMoveSound() {
        if (audioLoader != null) {
            audioLoader.playEffect(MOVE);
        }
    }

    public static void playSetBombSound() {
        if (audioLoader != null) {
            audioLoader.playEffect(SET_BOOM);
        }
    }

    public static void playBombExplosionSound() {
        if (audioLoader != null) {
            audioLoader.playEffect(BOOM_BANG);
        }
    }

    public static void playEatItemSound() {
        if (audioLoader != null) {
            audioLoader.playEffect(EAT_ITEM);
        }
    }
}
