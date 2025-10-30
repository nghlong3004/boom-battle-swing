package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.constant.ButtonConstant;
import io.nghlong3004.boom_battle_swing.util.ObjectContainer;
import io.nghlong3004.boom_battle_swing.view.GameApplication;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.MouseScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import io.nghlong3004.boom_battle_swing.view.scene.button.SoundButton;
import io.nghlong3004.boom_battle_swing.view.scene.button.VolumeButton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.SLIDER_BUTTON;
import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.VOLUME_BUTTON_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;

@Slf4j
public class AudioOptionComponent extends AbstractScene implements Scene, MouseScene {

    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;

    public AudioOptionComponent(GameApplication game) {
        super(game);
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
        if (isInButton(e, musicButton)) {
            musicButton.setMousePressed(true);
        }
        else if (isInButton(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        }
        else if (isInButton(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isInButton(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                ObjectContainer.getAudioPlayer().toggleSongMute();
            }
        }
        else if (isInButton(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                ObjectContainer.getAudioPlayer().toggleEffectMute();
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
                ObjectContainer.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);
        if (isInButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        }
        else if (isInButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
        else if (isInButton(e, volumeButton)) {
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
    public void draw(Graphics g) {
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeButton.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
