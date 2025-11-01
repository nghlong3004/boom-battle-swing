package io.nghlong3004.view.component;

import io.nghlong3004.constant.ButtonConstant;
import io.nghlong3004.util.ObjectContainer;
import io.nghlong3004.view.button.SoundButton;
import io.nghlong3004.view.button.VolumeButton;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.ButtonConstant.SLIDER_BUTTON;
import static io.nghlong3004.constant.ButtonConstant.VOLUME_BUTTON_HEIGHT;
import static io.nghlong3004.constant.GameConstant.SCALE;

public class AudioComponent implements MouseComponent {

    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;

    public AudioComponent() {
        createSoundButton();
        createVolumeButton();
    }

    private void createSoundButton() {
        int soundX = (int) (450 * SCALE);
        int musicY = (int) (140 * SCALE);
        int sfxY = (int) (186 * SCALE);
        musicButton = new SoundButton(soundX, musicY, ButtonConstant.SOUND_BUTTON_SIZE,
                                      ButtonConstant.SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, ButtonConstant.SOUND_BUTTON_SIZE, ButtonConstant.SOUND_BUTTON_SIZE);
    }

    private void createVolumeButton() {
        int vX = (int) (309 * SCALE);
        int vY = (int) (278 * SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_BUTTON, VOLUME_BUTTON_HEIGHT);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (musicButton.isMouseOver(e)) {
            musicButton.setMousePressed(true);
        }
        else if (sfxButton.isMouseOver(e)) {
            sfxButton.setMousePressed(true);
        }
        else if (volumeButton.isMouseOver(e)) {
            volumeButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (musicButton.isMouseOver(e)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                ObjectContainer.getAudioUtil().toggleSongMute();
            }
        }
        else if (sfxButton.isMouseOver(e)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                ObjectContainer.getAudioUtil().toggleEffectMute();
            }
        }

        musicButton.reset();
        sfxButton.reset();
        volumeButton.reset();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeButtonX(e.getX());
            float valueAfter = volumeButton.getFloatValue();
            if (valueBefore != valueAfter) {
                ObjectContainer.getAudioUtil().setVolume(valueAfter);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);
        if (musicButton.isMouseOver(e)) {
            musicButton.setMouseOver(true);
        }
        else if (sfxButton.isMouseOver(e)) {
            sfxButton.setMouseOver(true);
        }
        else if (volumeButton.isMouseOver(e)) {
            volumeButton.setMouseOver(true);
        }
    }

    @Override
    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    @Override
    public void render(Graphics g) {
        musicButton.render(g);
        sfxButton.render(g);
        volumeButton.render(g);
    }

}
