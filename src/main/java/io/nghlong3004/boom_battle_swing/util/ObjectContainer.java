package io.nghlong3004.boom_battle_swing.util;

import io.nghlong3004.boom_battle_swing.audio.AudioPlayer;

public class ObjectContainer {

    private static final AudioPlayer AUDIO_PLAYER = AudioPlayer.getInstance();

    private static final ImageContainer IMAGE_CONTAINER = ImageContainer.getInstance();

    public static AudioPlayer getAudioPlayer() {
        return AUDIO_PLAYER;
    }

    public static ImageContainer getImageContainer() {
        return IMAGE_CONTAINER;
    }

}
