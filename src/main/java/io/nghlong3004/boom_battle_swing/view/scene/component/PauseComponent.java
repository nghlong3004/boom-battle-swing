package io.nghlong3004.boom_battle_swing.view.scene.component;

import io.nghlong3004.boom_battle_swing.constant.ButtonConstant;
import io.nghlong3004.boom_battle_swing.constant.GameConstant;
import io.nghlong3004.boom_battle_swing.constant.ImageConstant;
import io.nghlong3004.boom_battle_swing.util.ImageUtil;
import io.nghlong3004.boom_battle_swing.view.scene.AbstractScene;
import io.nghlong3004.boom_battle_swing.view.scene.GameState;
import io.nghlong3004.boom_battle_swing.view.scene.MouseScene;
import io.nghlong3004.boom_battle_swing.view.scene.Scene;
import io.nghlong3004.boom_battle_swing.view.scene.button.SoundButton;
import io.nghlong3004.boom_battle_swing.view.scene.button.URMButton;
import io.nghlong3004.boom_battle_swing.view.scene.button.VolumeButton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.boom_battle_swing.constant.ButtonConstant.*;
import static io.nghlong3004.boom_battle_swing.constant.GameConstant.SCALE;

@Slf4j
public class PauseComponent extends AbstractScene implements Scene, MouseScene {

    private BufferedImage background;
    private int xBackground, yBackground, widthBackground, heightBackground;
    private SoundButton musicButton, sfxButton;
    private URMButton homeButton, replayButton, unpauseButton;
    private VolumeButton volumeButton;
    private final PlayingComponent playing;

    public PauseComponent(PlayingComponent playing) {
        super(playing.getGame());
        this.playing = playing;
        loadBackground();
        createSoundButton();
        createURMButton();
        createVolumButton();
    }

    private void createVolumButton() {
        int vX = (int) (309 * SCALE);
        int vY = (int) (278 * SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_BUTTON, VOLUME_BUTTON_HEIGHT);
    }

    private void createURMButton() {
        int homeX = (int) (321 * SCALE);
        int unPauseX = (int) (homeX + URM_BUTTON_SIZE * 6 / 5);
        int relayX = (int) (unPauseX + URM_BUTTON_SIZE * 6 / 5);
        int urmY = (int) (325 * SCALE);
        unpauseButton = new URMButton(unPauseX, urmY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        replayButton = new URMButton(relayX, urmY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
        homeButton = new URMButton(homeX, urmY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    private void createSoundButton() {
        int soundX = (int) (450 * SCALE);
        int musicY = (int) (140 * SCALE);
        int sfxY = (int) (186 * SCALE);
        musicButton = new SoundButton(soundX, musicY, ButtonConstant.SOUND_BUTTON_SIZE,
                                      ButtonConstant.SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, ButtonConstant.SOUND_BUTTON_SIZE, ButtonConstant.SOUND_BUTTON_SIZE);
    }

    private void loadBackground() {
        background = ImageUtil.loadImage(ImageConstant.PAUSE_BACKGROUND);
        widthBackground = (int) (background.getWidth() * SCALE);
        heightBackground = (int) (background.getHeight() * SCALE);
        xBackground = GameConstant.GAME_WIDTH - widthBackground >>> 1;
        yBackground = GameConstant.GAME_HEIGHT - heightBackground >>> 1;
    }


    @Override
    public void update() {
        musicButton.update();
        sfxButton.update();
        homeButton.update();
        unpauseButton.update();
        replayButton.update();
        volumeButton.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background, xBackground, yBackground, widthBackground, heightBackground, null);
        musicButton.draw(g);
        sfxButton.draw(g);
        homeButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        volumeButton.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isInButton(e, musicButton)) {
            musicButton.setMousePressed(true);
        }
        if (isInButton(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        }

        if (isInButton(e, homeButton)) {
            homeButton.setMousePressed(true);
        }
        else if (isInButton(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        }
        else if (isInButton(e, replayButton)) {
            replayButton.setMousePressed(true);
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
            }
        }
        if (isInButton(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }
        musicButton.reset();
        sfxButton.reset();
        if (isInButton(e, homeButton)) {
            if (homeButton.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.setPaused(true);
            }
        }
        else if (isInButton(e, unpauseButton)) {
            if (unpauseButton.isMousePressed()) {
                playing.setPaused(false);
            }
        }
        else if (isInButton(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                log.info("This is not work");
            }
        }
        homeButton.reset();
        unpauseButton.reset();
        replayButton.reset();
        volumeButton.reset();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        homeButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isInButton(e, musicButton)) {
            musicButton.setMouseOver(true);
        }
        else if (isInButton(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }

        if (isInButton(e, homeButton)) {
            homeButton.setMouseOver(true);
        }
        else if (isInButton(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        }
        else if (isInButton(e, replayButton)) {
            replayButton.setMouseOver(true);
        }
        else if (isInButton(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeButtonX(e.getX());
        }
    }
}
