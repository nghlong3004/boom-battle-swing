package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.constant.ButtonConstant;
import io.nghlong3004.boom_battle_swing.constant.GameConstant;
import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.MouseScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import io.nghlong3004.boom_battle_swing.view.scene.button.PauseButton;
import io.nghlong3004.boom_battle_swing.view.scene.button.SoundButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseComponent implements Scene, MouseScene {

    private BufferedImage background;
    private int xBackground, yBackground, widthBackground, heightBackground;
    private SoundButton musicButton, sfxButton;

    public PauseComponent() {
        loadBackground();
        createSoundButton();
    }

    private void createSoundButton() {
        int soundX = (int) (450 * GameConstant.SCALE);
        int musicY = (int) (140 * GameConstant.SCALE);
        int sfxY = (int) (186 * GameConstant.SCALE);
        musicButton = new SoundButton(soundX, musicY, ButtonConstant.SOUND_BUTTON_SIZE,
                                      ButtonConstant.SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, ButtonConstant.SOUND_BUTTON_SIZE, ButtonConstant.SOUND_BUTTON_SIZE);
    }

    private void loadBackground() {
        background = ImageUtil.loadImage(ImageConstant.PAUSE_BACKGROUND);
        widthBackground = (int) (background.getWidth() * GameConstant.SCALE);
        heightBackground = (int) (background.getHeight() * GameConstant.SCALE);
        xBackground = GameConstant.GAME_WIDTH - widthBackground >>> 1;
        yBackground = GameConstant.GAME_HEIGHT - heightBackground >>> 1;
    }


    @Override
    public void update() {
        musicButton.update();
        sfxButton.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background, xBackground, yBackground, widthBackground, heightBackground, null);
        musicButton.draw(g);
        sfxButton.draw(g);
    }

    private boolean isInPauseButton(MouseEvent e, PauseButton pauseButton) {
        return pauseButton.getHitbox().contains(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isInPauseButton(e, musicButton)) {
            musicButton.setMousePressed(true);
        }
        if (isInPauseButton(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isInPauseButton(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        }
        if (isInPauseButton(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }
        musicButton.reset();
        sfxButton.reset();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        if (isInPauseButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        }
        if (isInPauseButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }

    }
}
