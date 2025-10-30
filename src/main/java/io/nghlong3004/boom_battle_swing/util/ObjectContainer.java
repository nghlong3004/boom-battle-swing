package io.nghlong3004.boom_battle_swing.util;

import io.nghlong3004.boom_battle_swing.audio.AudioPlayer;

public class ObjectContainer {

    private static final AudioPlayer AUDIO_PLAYER = AudioPlayer.getInstance();

    public static AudioPlayer getAudioPlayer() {
        return AUDIO_PLAYER;
    }

}
