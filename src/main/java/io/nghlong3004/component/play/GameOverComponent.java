package io.nghlong3004.component.play;

import io.nghlong3004.component.button.GameButton;
import io.nghlong3004.component.button.SpriteButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.PlayingState;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.PlayType;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;
import static io.nghlong3004.constant.GameConstant.SCALE;

@Slf4j
public class GameOverComponent extends PlayComponent {
    private SpriteButton replayButton;
    private SpriteButton homeButton;

    private final List<GameButton> buttons;

    private BufferedImage loseImage;
    private int animationTick = 0;
    private float imageAlpha = 0f;
    private float imageScale = 0.5f;
    private boolean scaleGrowing = true;

    public GameOverComponent(GameContext context) {
        super(context);
        loadImages();
        createSpritesButton();
        buttons = List.of(replayButton, homeButton);
    }

    private void loadImages() {
        try {
            loseImage = ImageIO.read(getClass().getResourceAsStream("/images/component/lose.png"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to load lose image");
        }
    }

    public void resetAnimation() {
        animationTick = 0;
        imageAlpha = 0f;
        imageScale = 0.5f;
        scaleGrowing = true;
    }

    private void createSpritesButton() {
        int buttonSpacing = (int) (URM_BUTTON_SIZE * 6 / 5);
        int totalWidth = URM_BUTTON_SIZE * 2 + buttonSpacing;
        int startX = (GAME_WIDTH - totalWidth + URM_BUTTON_SIZE) / 2;
        int spriteY = (int) (350 * SCALE);

        homeButton = new SpriteButton(startX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
        replayButton = new SpriteButton(startX + buttonSpacing, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                PlayingState playingState = (PlayingState) context.getGameState(GameStateType.PLAYING);
                if (playingState != null) {
                    ((GamePlayComponent) playingState.getComponent(PlayType.PLAYING)).exit();
                }
                context.changeState(GameStateType.MENU);
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                PlayingState playingState = (PlayingState) context.getGameState(GameStateType.PLAYING);
                if (playingState != null) {
                    ((GamePlayComponent) playingState.getComponent(PlayType.PLAYING)).play();
                    playingState.setType(PlayType.PLAYING);
                }
                resetAnimation();
                log.info("Game reset from Game Over, restarting...");
            }
        }

        for (var gameButton : buttons) {
            gameButton.reset();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var gameButton : buttons) {
            gameButton.setMouseOver(false);
        }
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void update() {
        for (var gameObject : buttons) {
            gameObject.update();
        }
        animationTick++;
        if (imageAlpha < 1f) {
            imageAlpha += 0.02f;
            if (imageAlpha > 1f) {
                imageAlpha = 1f;
            }
        }
        if (animationTick < 40) {
            imageScale += 0.035f;
            if (imageScale > 1.2f) {
                imageScale = 1.2f;
                scaleGrowing = false;
            }
        }
        else if (animationTick < 60) {
            imageScale -= 0.01f;
            if (imageScale < 1.0f) {
                imageScale = 1.0f;
            }
        }
        else {

            float pulseAmount = (float) Math.sin(animationTick * 0.05) * 0.03f;
            imageScale = 1.0f + pulseAmount;
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (loseImage != null) {
            int originalWidth = loseImage.getWidth();
            int originalHeight = loseImage.getHeight();

            int scaledWidth = (int) (originalWidth * SCALE * imageScale);
            int scaledHeight = (int) (originalHeight * SCALE * imageScale);

            int imageX = (GAME_WIDTH - scaledWidth) / 2;
            int imageY = (int) (150 * SCALE);

            int alpha = (int) (imageAlpha * 255);
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha);
            g2d.setComposite(alphaComposite);

            if (imageAlpha > 0.5f) {
                AlphaComposite shadowComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha * 0.4f);
                g2d.setComposite(shadowComposite);
                g2d.drawImage(loseImage, imageX + 5, imageY + 5, scaledWidth, scaledHeight, null);

                g2d.setComposite(alphaComposite);
            }

            g2d.drawImage(loseImage, imageX, imageY, scaledWidth, scaledHeight, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        for (var gameObject : buttons) {
            gameObject.render(g);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
