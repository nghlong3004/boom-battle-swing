package io.nghlong3004.component.option;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.button.SoundButton;
import io.nghlong3004.component.button.VolumeButton;
import io.nghlong3004.constant.ButtonConstant;
import io.nghlong3004.context.GameContext;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.ButtonConstant.*;
import static io.nghlong3004.constant.GameConstant.*;

public class AudioComponent extends GameComponent {
    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;

    public AudioComponent(GameContext context) {
        super(context);
        createSoundButton();
        createVolumeButton();
    }

    private void createSoundButton() {
        int soundX = GAME_WIDTH + ButtonConstant.SOUND_BUTTON_SIZE >>> 1;
        int musicY = GAME_HEIGHT / 4 + SOUND_BUTTON_SIZE / 2;
        int sfxY = GAME_HEIGHT / 4 + SOUND_BUTTON_SIZE / 2 + SOUND_BUTTON_SIZE;
        musicButton = new SoundButton(soundX, musicY, ButtonConstant.SOUND_BUTTON_SIZE,
                                      ButtonConstant.SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, ButtonConstant.SOUND_BUTTON_SIZE, ButtonConstant.SOUND_BUTTON_SIZE);
    }

    private void createVolumeButton() {
        int vX = GAME_WIDTH - SLIDER_BUTTON >>> 1;
        int vY = (int) (262 * SCALE);
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
                context.getAudio()
                       .toggleSongMute();
            }
        }
        else if (sfxButton.isMouseOver(e)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                context.getAudio()
                       .toggleEffectMute();
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
                context.getAudio()
                       .setVolume(valueAfter);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
