package io.nghlong3004.boom_battle_swing.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.CLICK;
import static io.nghlong3004.boom_battle_swing.constant.AudioConstant.VOLUME_START;

public class AudioPlayer {

    private Clip[] songs, effects;
    private volatile int currentSongId = 0;
    private volatile float volume = VOLUME_START;
    private volatile boolean songMute, effectMute;
    private volatile boolean wasPlayingBeforeMute;

    private final ExecutorService audioExec = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "audio-exec");
            t.setDaemon(true);
            return t;
        }
    });

    private static class Holder {
        private static final AudioPlayer INSTANCE = new AudioPlayer();
    }

    public static AudioPlayer getInstance() {
        return Holder.INSTANCE;
    }

    private AudioPlayer() {
        loadSongs();
        loadEffects();
        audioExec.execute(() -> {
            safeUpdateSongVolume();
            safeUpdateEffectsVolume();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(this::close, "audio-shutdown"));
    }

    private void loadSongs() {
        String[] names = {"soundMenu", "soundGame", "bye_bye"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
    }

    private void loadEffects() {
        String[] effectNames = {"move", "set_boom", "start", "click"};
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectNames[i]);
        }
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/sounds/" + name + ".wav");
        if (url == null) {
            throw new RuntimeException("Audio file not found: " + name);
        }
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(url)) {
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("Cannot load audio: " + name, e);
        }
    }


    public void setVolume(float volume) {
        if (volume < 0f) {
            volume = 0f;
        }
        if (volume > 1f) {
            volume = 1f;
        }
        final float newVol = volume;
        this.volume = newVol;
        audioExec.execute(() -> {
            safeUpdateSongVolume();
            safeUpdateEffectsVolume();
        });
    }

    public void stopSong() {
        audioExec.execute(() -> {
            Clip c = songs[currentSongId];
            if (c != null && c.isActive()) {
                c.stop();
                c.setMicrosecondPosition(0);
            }
        });
    }

    public void playEffect(int effect) {
        audioExec.execute(() -> {
            if (effect < 0 || effect >= effects.length) {
                return;
            }
            Clip c = effects[effect];
            if (c == null) {
                return;
            }
            if (c.isActive()) {
                c.stop();
            }
            c.setMicrosecondPosition(0);
            setClipMuteIfSupported(c, effectMute);
            c.start();
        });
    }

    public void playSong(int song) {
        audioExec.execute(() -> {
            if (song < 0 || song >= songs.length) {
                return;
            }
            Clip cur = songs[currentSongId];
            if (cur != null && cur.isActive()) {
                cur.stop();
            }

            currentSongId = song;
            Clip next = songs[currentSongId];
            if (next == null) {
                return;
            }

            safeUpdateSongVolume();
            setClipMuteIfSupported(next, songMute);

            next.setMicrosecondPosition(0);
            next.loop(Clip.LOOP_CONTINUOUSLY);
            wasPlayingBeforeMute = !songMute;
        });
    }

    public void toggleSongMute() {
        audioExec.execute(() -> {
            songMute = !songMute;
            Clip cur = songs[currentSongId];
            if (cur == null) {
                return;
            }

            if (songMute) {
                wasPlayingBeforeMute = cur.isActive();
                setClipMuteIfSupported(cur, true);
                if (cur.isActive()) {
                    cur.stop();
                }
            }
            else {
                setClipMuteIfSupported(cur, false);
                if (wasPlayingBeforeMute) {
                    cur.setMicrosecondPosition(0);
                    cur.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        });
    }

    public void toggleEffectMute() {
        audioExec.execute(() -> {
            effectMute = !effectMute;
            for (Clip c : effects) {
                setClipMuteIfSupported(c, effectMute);
            }
            if (!effectMute) {
                playEffect(CLICK);
            }
        });
    }

    public void close() {
        try {
            audioExec.shutdownNow();
        } catch (Exception ignored) {
        }
        safeCloseClips(songs);
        safeCloseClips(effects);
    }

    private void safeUpdateSongVolume() {
        Clip c = songs[currentSongId];
        if (c != null) {
            setClipVolumeIfSupported(c, volume);
        }
    }

    private void safeUpdateEffectsVolume() {
        for (Clip c : effects) {
            setClipVolumeIfSupported(c, volume);
        }
    }

    private static void setClipMuteIfSupported(Clip c, boolean mute) {
        if (c == null) {
            return;
        }
        if (c.isControlSupported(BooleanControl.Type.MUTE)) {
            BooleanControl bc = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            bc.setValue(mute);
        }
    }

    private static void setClipVolumeIfSupported(Clip c, float vol01) {
        if (c == null) {
            return;
        }
        if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float dB;
            if (vol01 <= 0f) {
                dB = gc.getMinimum();
            }
            else {
                dB = (float) (20.0 * Math.log10(vol01));
                if (dB < gc.getMinimum()) {
                    dB = gc.getMinimum();
                }
                if (dB > gc.getMaximum()) {
                    dB = gc.getMaximum();
                }
            }
            gc.setValue(dB);
        }
        else if (c.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl vc = (FloatControl) c.getControl(FloatControl.Type.VOLUME);
            vc.setValue(Math.max(0f, Math.min(1f, vol01)));
        }
    }

    private static void safeCloseClips(Clip[] arr) {
        if (arr == null) {
            return;
        }
        for (Clip c : arr) {
            if (c == null) {
                continue;
            }
            try {
                if (c.isActive()) {
                    c.stop();
                }
                c.drain();
                c.close();
            } catch (Exception ignored) {
            }
        }
    }
}
