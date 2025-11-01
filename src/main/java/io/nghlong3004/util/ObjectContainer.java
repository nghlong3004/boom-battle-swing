package io.nghlong3004.util;

public class ObjectContainer {

    private static final AudioUtil AUDIO_UTIL = AudioUtil.getInstance();

    private static final ImageContainer IMAGE_CONTAINER = ImageContainer.getInstance();

    public static AudioUtil getAudioUtil() {
        return AUDIO_UTIL;
    }

    public static ImageContainer getImageContainer() {
        return IMAGE_CONTAINER;
    }

}
